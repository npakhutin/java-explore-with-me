package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRqDto;
import ru.practicum.ewm.service.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto addNewCategory(CategoryRqDto categoryRqDto) {
        Category category = categoryRepository.save(CategoryMapper.mapToCategory(categoryRqDto));
        return CategoryMapper.mapToDto(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> findAll(Integer start, Integer size) {
        PageRequest pageable = PageRequest.of(start, size, Sort.by(Category.Fields.id).ascending());
        return categoryRepository.findAll(pageable).stream().map(CategoryMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto findById(Long id) {
        return CategoryMapper.mapToDto(categoryRepository.findById(id)
                                                         .orElseThrow(() -> new NotFoundException(
                                                                 "Не найдена категория id = " + id)));
    }

    @Override
    public CategoryDto updateById(Long id, CategoryRqDto categoryRqDto) {
        Category category = categoryRepository.findById(id)
                                              .orElseThrow(() -> new NotFoundException(
                                                      "Не найдена категория id = " + id));
        category.setName(categoryRqDto.getName());
        return CategoryMapper.mapToDto(categoryRepository.save(category));
    }
}
