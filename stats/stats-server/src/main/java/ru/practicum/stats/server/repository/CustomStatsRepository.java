package ru.practicum.stats.server.repository;

import ru.practicum.stats.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomStatsRepository {
    List<StatsDto> stats(LocalDateTime start, LocalDateTime end, List<String> uriList, boolean unique);
}
