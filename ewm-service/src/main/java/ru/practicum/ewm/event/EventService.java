package ru.practicum.ewm.event;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.ewm.event.dto.AddEventRqDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.FindEventsAdminParams;
import ru.practicum.ewm.event.dto.FindEventsParams;
import ru.practicum.ewm.event.dto.UpdateEventRqDto;
import ru.practicum.ewm.event.model.EventSortOrder;

import java.util.List;

public interface EventService {
    EventFullDto addNewEvent(AddEventRqDto eventRqDto, Long initiatorId);

    List<EventShortDto> findUserEvents(Long initiatorId, Integer start, Integer size);

    EventFullDto updateEventAdmin(UpdateEventRqDto eventRqDto, Long eventId);

    EventFullDto findEventById(Long eventId, HttpServletRequest request);

    EventFullDto findUserEventById(Long initiatorId, Long eventId);

    List<EventFullDto> findEventsAdmin(FindEventsAdminParams params, Integer start, Integer size);

    List<EventShortDto> findEvents(FindEventsParams params,
                                   EventSortOrder sort,
                                   Integer start,
                                   Integer size,
                                   HttpServletRequest request);

    EventFullDto updateEventUser(UpdateEventRqDto eventRqDto, Long userId, Long eventId);
}
