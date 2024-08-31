package ru.practicum.ewm.service;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EwmStatsClient {
    private static StatsClient statsClient;
    private final StatsClient autowiredStatsClient;

    public static void hit(HttpServletRequest request) {
        statsClient.hit("ewm", request.getRequestURI(), request.getRemoteAddr());
    }

    public static List<StatsDto> stats(LocalDateTime start, LocalDateTime end, List<String> uriList, Boolean unique) {
        return statsClient.stats(start, end, uriList, unique);
    }

    @PostConstruct
    private void init() {
        statsClient = this.autowiredStatsClient;
    }
}
