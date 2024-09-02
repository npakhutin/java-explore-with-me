package ru.practicum.ewm.event.dto;

import java.time.LocalDateTime;
import java.util.List;

public record FindEventsParams(String text,
                               List<Integer> categories,
                               Boolean paid,
                               LocalDateTime rangeStart,
                               LocalDateTime rangeEnd,
                               Boolean onlyAvailable) {}
