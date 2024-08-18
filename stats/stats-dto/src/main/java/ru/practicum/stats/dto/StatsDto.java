package ru.practicum.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsDto {
    private String app;
    private String uri;
    private Long hits;

    public StatsDto(StatsDto other) {
        this.app = other.getApp();
        this.uri = other.getUri();
        this.hits = other.getHits();
    }
}
