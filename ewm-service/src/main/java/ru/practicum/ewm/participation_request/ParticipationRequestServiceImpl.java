package ru.practicum.ewm.participation_request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.participation_request.dto.ParticipationRequestDto;
import ru.practicum.ewm.participation_request.dto.ParticipationRequestStatusUpdateRq;
import ru.practicum.ewm.participation_request.dto.ParticipationRequestStatusUpdateRs;
import ru.practicum.ewm.participation_request.model.ParticipationRequest;
import ru.practicum.ewm.participation_request.model.ParticipationRequestStatus;
import ru.practicum.ewm.service.exception.ConflictException;
import ru.practicum.ewm.service.exception.NotFoundException;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository requestRepository;

    @Override
    public ParticipationRequestDto addNewParticipationRequest(Long requesterId, Long eventId) {
        User requester = userRepository.findById(requesterId)
                                       .orElseThrow(() -> new NotFoundException(
                                               "Не найден пользователь id = " + requesterId));
        Event event = eventRepository.findById(eventId)
                                     .orElseThrow(() -> new NotFoundException("Не найдено событие id = " + eventId));
        if (requester.equals(event.getInitiator())) {
            throw new ConflictException("Инициатор события равен запрашивающему участие",
                                        "Инициатор и запрашивающий участие не могут быть одним пользователем");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Попытка подать заявку на участие в неопубликованном событии",
                                        "Заявку можно подавать только на опубликованные события");
        }
        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new ConflictException("Превышен лимит заявок на событие",
                                        "Заявку можно подавать только, если свободные места");
        }
        ParticipationRequestStatus requestStatus = event.getRequestModeration() && event.getParticipantLimit() != 0 ?
                                                   ParticipationRequestStatus.PENDING :
                                                   ParticipationRequestStatus.CONFIRMED;
        ParticipationRequest request = requestRepository.save(ParticipationRequestMapper.mapToRequest(requester,
                                                                                                      event,
                                                                                                      requestStatus));
        if (request.getStatus() == ParticipationRequestStatus.CONFIRMED) {
            eventRepository.updateEventConfirmedRequests(event);
        }
        return ParticipationRequestMapper.maptoDto(request);
    }

    @Override
    public List<ParticipationRequestDto> findUserParticipationRequests(Long requesterId) {
        List<ParticipationRequest> foundRequests = requestRepository.findByRequesterId(requesterId);

        return foundRequests.stream().map(ParticipationRequestMapper::maptoDto).collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> findEventParticipationRequests(Long initiatorId, Long eventId) {
        List<ParticipationRequest> foundRequests = requestRepository.findByEventId(eventId);

        return foundRequests.stream().map(ParticipationRequestMapper::maptoDto).collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelUserParticipationRequest(Long requesterId, Long requestId) {
        ParticipationRequest foundRequest = requestRepository.findByIdAndRequesterId(requestId, requesterId);
        foundRequest.setStatus(ParticipationRequestStatus.CANCELED);
        return ParticipationRequestMapper.maptoDto(requestRepository.save(foundRequest));
    }

    @Override
    @Transactional(noRollbackFor = {ConflictException.class})
    public ParticipationRequestStatusUpdateRs updateEventParticipationRequestsStatus(Long initiatorId,
                                                                                     Long eventId,
                                                                                     ParticipationRequestStatusUpdateRq statusUpdateRq) {
        ParticipationRequestStatusUpdateRs result = new ParticipationRequestStatusUpdateRs();

        List<ParticipationRequest> foundRequests = requestRepository.findAllById(statusUpdateRq.getRequestIds());
        Event event = eventRepository.findById(eventId)
                                     .orElseThrow(() -> new NotFoundException("Не найдено событие id = " + eventId));
        // если пришел статус Отказать, то обновляем его у всех присланных запросов и обновляем кол-во подтверждений в событии
        if (statusUpdateRq.getStatus() == ParticipationRequestStatus.REJECTED) {
            for (ParticipationRequest request : foundRequests) {
                if (request.getStatus() == ParticipationRequestStatus.CONFIRMED) {
                    throw new ConflictException("Попытка отменить уже принятую заявку на участие",
                                                "Нельзя отменять уже принятые заявки на участие");
                }
                request.setStatus(ParticipationRequestStatus.REJECTED);
                result.getRejectedRequests().add(ParticipationRequestMapper.maptoDto(request));
            }
        } else if (statusUpdateRq.getStatus() == ParticipationRequestStatus.CONFIRMED) {
            // если пришло подтверждение участия, то берем количество свободных слотов из события
            int freeSlots = event.getParticipantLimit() - event.getConfirmedRequests();
            // иначе сохраняем статус Подтвердить у первых freeSlots заявок,
            for (ParticipationRequest request : foundRequests.subList(0, Math.min(freeSlots, foundRequests.size()))) {
                request.setStatus(ParticipationRequestStatus.CONFIRMED);
                result.getConfirmedRequests().add(ParticipationRequestMapper.maptoDto(request));
            }
            // остальным отказываем, обновляем кол-во свободных слотов в событии
            if (freeSlots < foundRequests.size()) {
                for (ParticipationRequest request : foundRequests.subList(freeSlots, foundRequests.size())) {
                    request.setStatus(ParticipationRequestStatus.REJECTED);
                    result.getRejectedRequests().add(ParticipationRequestMapper.maptoDto(request));
                }
                // если freeSlots = 0, то возвращаем ошибку, что мест больше нет
                if (freeSlots == 0) {
                    requestRepository.saveAll(foundRequests);
                    throw new ConflictException("Нет свободных слотов для подтверждения заявок",
                                                "Подтвердить заявки можно только, если есть свободные слоты");
                }
            }
        }
        requestRepository.saveAll(foundRequests);
        eventRepository.updateEventConfirmedRequests(event);
        return result;
    }
}
