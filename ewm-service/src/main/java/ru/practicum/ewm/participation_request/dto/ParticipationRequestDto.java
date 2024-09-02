package ru.practicum.ewm.participation_request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.service.serialization.JacksonConfig;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipationRequestDto {
    @JsonFormat(pattern = JacksonConfig.JSON_DATE_STRING_FORMAT)
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private String status;
}