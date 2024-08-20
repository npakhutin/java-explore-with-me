package ru.practicum.stats.server;

import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    HitDto hit(HitDto hitDto);

    List<StatsDto> stats(LocalDateTime start, LocalDateTime end, List<String> uriList, Boolean unique);
}
