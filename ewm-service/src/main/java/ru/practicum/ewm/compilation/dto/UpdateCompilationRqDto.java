package ru.practicum.ewm.compilation.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRqDto {
    private List<Long> events = new ArrayList<>();
    private Boolean pinned;
    @Size(min = 1, max = 50, message = "Заголовок подборки должен укладываться в диапазон 1-50 символов")
    private String title;
}
