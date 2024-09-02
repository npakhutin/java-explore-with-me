package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public record FindEventsAdminParams(List<Integer> users,
                                    List<EventState> states,
                                    List<Integer> categories,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd) {

}
