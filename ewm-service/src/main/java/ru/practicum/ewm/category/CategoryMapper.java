package ru.practicum.ewm.category;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRqDto;

public class CategoryMapper {
    public static Category mapToCategory(CategoryRqDto categoryRqDto) {
        return new Category(null, categoryRqDto.getName());
    }

    public static CategoryDto mapToDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
