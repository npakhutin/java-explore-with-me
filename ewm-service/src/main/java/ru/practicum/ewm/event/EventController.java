package ru.practicum.ewm.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.dto.AddEventRqDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.FindEventsAdminParams;
import ru.practicum.ewm.event.dto.FindEventsParams;
import ru.practicum.ewm.event.dto.UpdateEventRqDto;
import ru.practicum.ewm.event.model.EventSortOrder;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.service.constraints.EventPatcher;
import ru.practicum.ewm.service.serialization.JacksonConfig;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventService eventService;

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateEventAdmin(@PathVariable Long eventId,
                                         @RequestBody @Validated(EventPatcher.Admin.class)
                                         UpdateEventRqDto eventRqDto) {
        return eventService.updateEventAdmin(eventRqDto, eventId);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> findEventsAdmin(@RequestParam(name = "users", defaultValue = "") List<Integer> users,
                                              @RequestParam(name = "states", defaultValue = "") List<EventState> states,
                                              @RequestParam(name = "categories",
                                                            defaultValue = "") List<Integer> categories,
                                              @RequestParam(name = "rangeStart", defaultValue = "")
                                              @DateTimeFormat(pattern = JacksonConfig.JSON_DATE_STRING_FORMAT)
                                              LocalDateTime rangeStart,
                                              @RequestParam(name = "rangeEnd", defaultValue = "")
                                              @DateTimeFormat(pattern = JacksonConfig.JSON_DATE_STRING_FORMAT)
                                              LocalDateTime rangeEnd,
                                              @RequestParam(name = "from", defaultValue = "0") Integer start,
                                              @RequestParam(name = "size", defaultValue = "10") Integer size) {
        FindEventsAdminParams params = new FindEventsAdminParams(users, states, categories, rangeStart, rangeEnd);
        return eventService.findEventsAdmin(params, start, size);
    }

    @GetMapping("/users/{initiatorId}/events")
    public List<EventShortDto> findUserEvents(@PathVariable Long initiatorId,
                                              @RequestParam(name = "from", defaultValue = "0") Integer start,
                                              @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return eventService.findUserEvents(initiatorId, start, size);
    }

    @GetMapping("/users/{initiatorId}/events/{eventId}")
    public EventFullDto findUserEventById(@PathVariable Long initiatorId, @PathVariable Long eventId) {
        return eventService.findUserEventById(initiatorId, eventId);
    }

    @PostMapping("/users/{initiatorId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addNewEvent(@PathVariable Long initiatorId, @RequestBody @Validated AddEventRqDto eventRqDto) {
        return eventService.addNewEvent(eventRqDto, initiatorId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateEventUser(@PathVariable Long userId,
                                        @PathVariable Long eventId,
                                        @RequestBody @Validated(EventPatcher.Initiator.class)
                                        UpdateEventRqDto eventRqDto) {
        return eventService.updateEventUser(eventRqDto, userId, eventId);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto findEventById(@PathVariable Long eventId, HttpServletRequest request) {
        return eventService.findEventById(eventId, request);
    }

    @GetMapping("/events")
    public List<EventShortDto> findEvents(@RequestParam(name = "text", defaultValue = "%") String text,
                                          @RequestParam(name = "categories",
                                                        defaultValue = "") List<Integer> categories,
                                          @RequestParam(name = "paid", defaultValue = "") Boolean paid,
                                          @RequestParam(name = "rangeStart", defaultValue = "")
                                          @DateTimeFormat(pattern = JacksonConfig.JSON_DATE_STRING_FORMAT)
                                          LocalDateTime rangeStart,
                                          @RequestParam(name = "rangeEnd", defaultValue = "")
                                          @DateTimeFormat(pattern = JacksonConfig.JSON_DATE_STRING_FORMAT)
                                          LocalDateTime rangeEnd,
                                          @RequestParam(name = "onlyAvailable",
                                                        defaultValue = "") Boolean onlyAvailable,
                                          @RequestParam(name = "sort", defaultValue = "EVENT_DATE") EventSortOrder sort,
                                          @RequestParam(name = "from", defaultValue = "0") Integer start,
                                          @RequestParam(name = "size", defaultValue = "10") Integer size,
                                          HttpServletRequest request) {
        FindEventsParams params = new FindEventsParams(text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
        return eventService.findEvents(params,
                                       sort,
                                       start,
                                       size,
                                       request);
    }
}
