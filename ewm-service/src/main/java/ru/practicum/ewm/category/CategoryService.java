package ru.practicum.ewm.category;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRqDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addNewCategory(CategoryRqDto categoryRqDto);

    void deleteById(Long id);

    List<CategoryDto> findAll(Integer start, Integer size);

    CategoryDto findById(Long id);

    CategoryDto updateById(Long id, CategoryRqDto categoryRqDto);
}
