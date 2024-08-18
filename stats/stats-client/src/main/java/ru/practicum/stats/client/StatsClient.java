package ru.practicum.stats.client;

import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsClient {
    HitDto hit(String app, String uri, String ip);

    List<StatsDto> stats(LocalDateTime start, LocalDateTime end, List<String> uriList, Boolean unique);
}
