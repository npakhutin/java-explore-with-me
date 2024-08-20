package ru.practicum.stats.server;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;
import ru.practicum.stats.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;

    @Override
    public HitDto hit(HitDto hitDto) {
        Hit hit = Mapper.mapToHit(hitDto);
        return Mapper.mapToHitDto(repository.save(hit));
    }

    @Override
    public List<StatsDto> stats(LocalDateTime start, LocalDateTime end, List<String> uriList, Boolean unique) {

        return repository.stats(start, end, uriList, unique);
    }

}
