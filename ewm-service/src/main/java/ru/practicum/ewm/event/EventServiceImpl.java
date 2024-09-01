package ru.practicum.ewm.event;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.event.dto.AddEventRqDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.FindEventsAdminParams;
import ru.practicum.ewm.event.dto.FindEventsParams;
import ru.practicum.ewm.event.dto.UpdateEventRqDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventSortOrder;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.EventStateAction;
import ru.practicum.ewm.service.exception.BadRequestException;
import ru.practicum.ewm.service.exception.ConflictException;
import ru.practicum.ewm.service.exception.NotFoundException;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final StatsClient statsClient;
    private final EntityManager em;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private static DateRange populateRange(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        rangeStart = rangeStart == null ? LocalDateTime.now() : rangeStart;
        rangeEnd = rangeEnd == null ? LocalDateTime.of(9999, 12, 31, 23, 59) : rangeEnd;
        if (rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Неправильно задан период поиска");
        }
        return new DateRange(rangeStart, rangeEnd);
    }

    @Override
    public EventFullDto addNewEvent(AddEventRqDto eventRqDto, Long initiatorId) {
        Category category = categoryRepository.findById(eventRqDto.getCategory())
                                              .orElseThrow(() -> new NotFoundException(
                                                      "Не найдена категория id = " + eventRqDto.getCategory()));

        User initiator = userRepository.findById(initiatorId)
                                       .orElseThrow(() -> new NotFoundException(
                                               "Не найден пользователь id = " + initiatorId));

        Event event = eventRepository.save(EventMapper.mapToEvent(eventRqDto, category, initiator));
        return EventMapper.mapToFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> findUserEvents(Long initiatorId, Integer start, Integer size) {
        User initiator = userRepository.findById(initiatorId)
                                       .orElseThrow(() -> new NotFoundException(
                                               "Не найден пользователь id = " + initiatorId));

        int pageNumber = size != 0 ? start / size : 0;
        PageRequest pageable = PageRequest.of(pageNumber, size, Sort.by(Event.Fields.id).ascending());
        Page<Event> foundEvents = eventRepository.findByInitiator(initiator, pageable);

        return foundEvents.stream().map(EventMapper::mapToShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventAdmin(UpdateEventRqDto eventRqDto, Long eventId) {
        Event event = eventRepository.findById(eventId)
                                     .orElseThrow(() -> new NotFoundException("Не найдено событие id = " + eventId));

        commonUpdateEvent(eventRqDto, event);
        if (eventRqDto.getStateAction() != null) {
            if (eventRqDto.getStateAction() == EventStateAction.PUBLISH_EVENT) {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new ConflictException("Событие уже опубликовано",
                                                "Невозможно заново опубликовать уже опубликованное событие");
                } else if (event.getState() == EventState.CANCELED) {
                    throw new ConflictException("Событие ранее было отменено",
                                                "Невозможно опубликовать отмененное событие");
                }
                event.setState(EventState.PUBLISHED);
            } else if (eventRqDto.getStateAction() == EventStateAction.REJECT_EVENT) {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new ConflictException("Событие ранее было опубликовано",
                                                "Невозможно отменить уже опубликованное событие");
                }
                event.setState(EventState.CANCELED);
            }
        }

        return EventMapper.mapToFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto findEventById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                                     .orElseThrow(() -> new NotFoundException("Не найдено событие id = " + eventId));
        statsClient.hit("ewm", request.getRequestURI(), request.getRemoteAddr());
        List<StatsDto> stats = statsClient.stats(event.getCreatedOn(),
                                                    LocalDateTime.now(),
                                                    List.of(request.getRequestURI()),
                                                    true);
        if (!stats.isEmpty()) {
            event.setViews(stats.getFirst().getHits());
        }
        return EventMapper.mapToFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto findUserEventById(Long initiatorId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, initiatorId)
                                     .orElseThrow(() -> new NotFoundException("Не найдено событие id = " + eventId));
        return EventMapper.mapToFullDto(event);
    }

    @Override
    public List<EventFullDto> findEventsAdmin(FindEventsAdminParams params,
                                              Integer start,
                                              Integer size) {
        DateRange dateRange = populateRange(params.rangeStart(), params.rangeEnd());

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> q = cb.createQuery(Event.class);

        Root<Event> e = q.from(Event.class);
        e.fetch(Event.Fields.initiator, JoinType.LEFT);
        e.fetch(Event.Fields.category, JoinType.LEFT);
        q.select(e);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.and(cb.greaterThanOrEqualTo(e.get(Event.Fields.eventDate), dateRange.start()),
                              cb.lessThanOrEqualTo(e.get(Event.Fields.eventDate), dateRange.end())));
        if (params.users() != null && !params.users().isEmpty()) {
            predicates.add(e.get(Event.Fields.initiator).get(User.Fields.id).in(params.users()));
        }
        if (params.states() != null && !params.states().isEmpty()) {
            predicates.add(e.get(Event.Fields.state).in(params.states()));
        }
        if (params.categories() != null && !params.categories().isEmpty()) {
            predicates.add(e.get(Event.Fields.category).get(Category.Fields.id).in(params.categories()));
        }

        q.where(predicates.toArray(new Predicate[]{}));
        q.orderBy(cb.asc(e.get(Event.Fields.id)));

        TypedQuery<Event> tq = em.createQuery(q);
        tq.setFirstResult(start);
        tq.setMaxResults(size);
        List<Event> foundEvents = tq.getResultList();

        return foundEvents.stream().map(EventMapper::mapToFullDto).collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> findEvents(FindEventsParams params,
                                          EventSortOrder sort,
                                          Integer start,
                                          Integer size,
                                          HttpServletRequest request) {
        DateRange dateRange = populateRange(params.rangeStart(), params.rangeEnd());

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> q = cb.createQuery(Event.class);

        Root<Event> e = q.from(Event.class);
        e.fetch(Event.Fields.category, JoinType.LEFT);
        q.select(e);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.or(cb.like(cb.upper(e.get(Event.Fields.annotation)), params.text().toUpperCase()),
                             cb.like(cb.upper(e.get(Event.Fields.description)), params.text().toUpperCase())));
        predicates.add(cb.equal(e.get(Event.Fields.state), EventState.PUBLISHED));
        if (params.paid() != null) {
            predicates.add(cb.equal(e.get(Event.Fields.paid), params.paid()));
        }
        predicates.add(cb.and(cb.greaterThanOrEqualTo(e.get(Event.Fields.eventDate), dateRange.start()),
                              cb.lessThanOrEqualTo(e.get(Event.Fields.eventDate), dateRange.end())));
        if (params.categories() != null && !params.categories().isEmpty()) {
            predicates.add(e.get(Event.Fields.category).get(Category.Fields.id).in(params.categories()));
        }
        if (params.onlyAvailable() != null && params.onlyAvailable()) {
            predicates.add(cb.lessThan(e.get(Event.Fields.confirmedRequests), e.get(Event.Fields.participantLimit)));
        }
        if (sort == EventSortOrder.EVENT_DATE) {
            q.orderBy(cb.asc(e.get(Event.Fields.eventDate)));
        }

        q.where(predicates.toArray(new Predicate[]{}));
        q.orderBy(cb.asc(e.get(Event.Fields.id)));

        TypedQuery<Event> tq = em.createQuery(q);
        tq.setFirstResult(start);
        tq.setMaxResults(size);
        List<Event> foundEvents = tq.getResultList();

        statsClient.hit("ewm", request.getRequestURI(), request.getRemoteAddr());

        Map<String, Event> uriList = new HashMap<>();
        LocalDateTime eventMinDateCreated = LocalDateTime.now();
        for (Event event : foundEvents) {
            uriList.put("/events/" + event.getId(), event);
            eventMinDateCreated = event.getCreatedOn().isBefore(eventMinDateCreated) ?
                                  event.getCreatedOn() :
                                  eventMinDateCreated;
        }
        List<StatsDto> stats = statsClient.stats(eventMinDateCreated,
                                                    LocalDateTime.now(),
                                                    new ArrayList<>(uriList.keySet()),
                                                    true);
        for (StatsDto statsDto : stats) {
            Event event = uriList.get(statsDto.getUri());
            event.setViews(statsDto.getHits());
        }

        if (sort == EventSortOrder.VIEWS && !foundEvents.isEmpty()) {
            foundEvents.sort(Comparator.comparing(Event::getViews, Comparator.reverseOrder())
                                       .thenComparing(Event::getEventDate));
        }

        return foundEvents.stream().map(EventMapper::mapToShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventUser(UpdateEventRqDto eventRqDto, Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                                     .orElseThrow(() -> new NotFoundException("Не найдено событие id = " + eventId));
        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Попытка изменения уже опубликованного события",
                                        "Пользователь не может редактировать уже опубликованное событие");
        }
        commonUpdateEvent(eventRqDto, event);
        if (eventRqDto.getStateAction() != null) {
            if (eventRqDto.getStateAction() == EventStateAction.SEND_TO_REVIEW) {
                event.setState(EventState.PENDING);
            } else if (eventRqDto.getStateAction() == EventStateAction.CANCEL_REVIEW) {
                event.setState(EventState.CANCELED);
            }
        }

        return EventMapper.mapToFullDto(eventRepository.save(event));
    }

    private void commonUpdateEvent(UpdateEventRqDto eventRqDto, Event event) {
        if (eventRqDto.getAnnotation() != null) {
            event.setAnnotation(eventRqDto.getAnnotation());
        }
        if (eventRqDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventRqDto.getCategory())
                                                  .orElseThrow(() -> new NotFoundException(
                                                          "Не найдена категория id = " + eventRqDto.getCategory()));
            event.setCategory(category);
        }
        if (eventRqDto.getDescription() != null) {
            event.setDescription(eventRqDto.getDescription());
        }
        if (eventRqDto.getEventDate() != null) {
            event.setEventDate(eventRqDto.getEventDate());
        }
        if (eventRqDto.getLocation() != null) {
            event.setLocation(eventRqDto.getLocation());
        }
        if (eventRqDto.getPaid() != null) {
            event.setPaid(eventRqDto.getPaid());
        }
        if (eventRqDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventRqDto.getParticipantLimit());
        }
        if (eventRqDto.getRequestModeration() != null) {
            event.setRequestModeration(eventRqDto.getRequestModeration());
        }
        if (eventRqDto.getTitle() != null) {
            event.setTitle(eventRqDto.getTitle());
        }
    }

    private record DateRange(LocalDateTime start, LocalDateTime end) {}
}
