package ru.practicum.stats.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto hit(@RequestBody HitDto hitDto) {
        log.info("hit {}", hitDto);
        return service.hit(hitDto);
    }

    @GetMapping("/stats")
    public List<StatsDto> stats(
            @RequestParam(name = "start", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime start,
            @RequestParam(name = "end", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime end,
            @RequestParam(name = "uris", required = false) List<String> uriList,
            @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        log.info("stats {}, {}, {}, {}", start, end, uriList, unique);
        return service.stats(start, end, uriList, unique);
    }
}
