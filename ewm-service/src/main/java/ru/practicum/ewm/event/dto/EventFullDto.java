package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.service.serialization.JacksonConfig;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventFullDto {
    private Long id;
    private String annotation;
    private Category category;
    private Integer confirmedRequests;
    @JsonFormat(pattern = JacksonConfig.JSON_DATE_STRING_FORMAT)
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(pattern = JacksonConfig.JSON_DATE_STRING_FORMAT)
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    @JsonFormat(pattern = JacksonConfig.JSON_DATE_STRING_FORMAT)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private String state;
    private String title;
    private Long views;
}
