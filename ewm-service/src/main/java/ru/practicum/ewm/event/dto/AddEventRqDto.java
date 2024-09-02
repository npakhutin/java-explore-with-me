package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.service.constraints.IntervalTillEvent;
import ru.practicum.ewm.service.serialization.JacksonConfig;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddEventRqDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "Краткое описание события должно укладываться в диапазон 20-2000 символов")
    private String annotation;
    @Min(1L)
    private Long category;
    @NotBlank
    @Size(min = 20, max = 7000, message = "Полное описание события должно укладываться в диапазон 20-7000 символов")
    private String description;
    @IntervalTillEvent(strInterval = "PT2H")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JacksonConfig.JSON_DATE_STRING_FORMAT)
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    @Min(0)
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotBlank
    @Size(min = 3, max = 120, message = "Заголовок события должно укладываться в диапазон 3-120 символов")
    private String title;
}
