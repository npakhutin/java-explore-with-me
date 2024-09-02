package ru.practicum.ewm.participation_request;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.participation_request.dto.ParticipationRequestDto;
import ru.practicum.ewm.participation_request.model.ParticipationRequest;
import ru.practicum.ewm.participation_request.model.ParticipationRequestStatus;
import ru.practicum.ewm.user.User;

import java.time.LocalDateTime;

public class ParticipationRequestMapper {
    public static ParticipationRequest mapToRequest(User requester,
                                                    Event event,
                                                    ParticipationRequestStatus requestStatus) {
        return ParticipationRequest.builder()
                                   .id(null)
                                   .status(requestStatus)
                                   .requester(requester)
                                   .event(event)
                                   .created(LocalDateTime.now())
                                   .build();
    }

    public static ParticipationRequestDto maptoDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                                      .id(request.getId())
                                      .requester(request.getRequester().getId())
                                      .created(request.getCreated())
                                      .status(request.getStatus().name())
                                      .event(request.getEvent().getId())
                                      .build();
    }
}
