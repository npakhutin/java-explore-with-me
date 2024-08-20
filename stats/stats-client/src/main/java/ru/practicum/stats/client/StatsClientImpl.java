package ru.practicum.stats.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsClientImpl extends BaseClient implements StatsClient {
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    public StatsClientImpl(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                      .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                      .build());
    }

    @Override
    public HitDto hit(String app, String uri, String ip) {
        HitDto hitDto = new HitDto(null, app, uri, ip, LocalDateTime.now());
        ResponseEntity<Object> responseEntity = post("/hit", hitDto);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Произошла ошибка " + responseEntity.getStatusCode());
        }
        return mapper.convertValue(responseEntity.getBody(), HitDto.class);
    }

    @Override
    public List<StatsDto> stats(@NonNull LocalDateTime start,
                                @NonNull LocalDateTime end,
                                List<String> uriList,
                                Boolean unique) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        parameters.put("end", end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        parameters.put("uris", uriList);
        parameters.put("unique", unique);

        ResponseEntity<Object> responseEntity = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                                                    parameters);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Произошла ошибка " + responseEntity.getStatusCode());
        }

        return mapper.convertValue(responseEntity.getBody(), new TypeReference<>() {});
    }
}
