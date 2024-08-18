package ru.practicum.stats.server;

import ru.practicum.stats.dto.HitDto;

public class Mapper {
    public static Hit mapToHit(HitDto hitDto) {
        return new Hit(hitDto.getId(), hitDto.getApp(), hitDto.getUri(), hitDto.getIp(), hitDto.getTimestamp());
    }

    public static HitDto mapToHitDto(Hit hit) {
        return new HitDto(hit.getId(), hit.getApp(), hit.getUri(), hit.getIp(), hit.getTimestamp());
    }
}
