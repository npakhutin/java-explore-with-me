package ru.practicum.ewm.event.mapper;

import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.event.dto.AddEventRqDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserMapper;

import java.time.LocalDateTime;
import java.util.Optional;

public class EventMapper {
    public static Event mapToEvent(AddEventRqDto eventRqDto, Category category, User initiator) {
        return Event.builder()
                    .id(null)
                    .annotation(eventRqDto.getAnnotation())
                    .category(category)
                    .confirmedRequests(0)
                    .createdOn(LocalDateTime.now())
                    .description(eventRqDto.getDescription())
                    .eventDate(eventRqDto.getEventDate())
                    .initiator(initiator)
                    .location(eventRqDto.getLocation())
                    .paid(Optional.ofNullable(eventRqDto.getPaid()).orElse(false))
                    .participantLimit(Optional.ofNullable(eventRqDto.getParticipantLimit()).orElse(0))
                    .publishedOn(null)
                    .requestModeration(Optional.ofNullable(eventRqDto.getRequestModeration()).orElse(true))
                    .state(EventState.PENDING)
                    .title(eventRqDto.getTitle())
                    .build();
    }

    public static EventFullDto mapToFullDto(Event event) {
        return EventFullDto.builder()
                           .id(event.getId())
                           .annotation(event.getAnnotation())
                           .category(event.getCategory())
                           .confirmedRequests(event.getConfirmedRequests())
                           .createdOn(event.getCreatedOn())
                           .description(event.getDescription())
                           .eventDate(event.getEventDate())
                           .initiator(UserMapper.mapToShortDto(event.getInitiator()))
                           .location(event.getLocation())
                           .paid(event.getPaid())
                           .participantLimit(event.getParticipantLimit())
                           .publishedOn(event.getPublishedOn())
                           .requestModeration(event.getRequestModeration())
                           .state(event.getState().name())
                           .title(event.getTitle())
                           .views(event.getViews())
                           .build();
    }

    public static EventShortDto mapToShortDto(Event event) {
        return EventShortDto.builder()
                            .annotation(event.getAnnotation())
                            .category(event.getCategory())
                            .confirmedRequests(event.getConfirmedRequests())
                            .eventDate(event.getEventDate())
                            .id(event.getId())
                            .initiator(UserMapper.mapToShortDto(event.getInitiator()))
                            .paid(event.getPaid())
                            .title(event.getTitle())
                            .views(event.getViews())
                            .build();
    }
}
