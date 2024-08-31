package ru.practicum.ewm.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRqDto {
    @NotBlank(message = "Имя категории не может быть пустым")
    @Size(min = 1, max = 50, message = "Имя категории должно укладываться в диапазон 1-50 символов")
    private String name;
}
