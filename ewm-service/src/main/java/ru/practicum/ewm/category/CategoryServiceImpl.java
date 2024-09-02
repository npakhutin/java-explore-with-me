package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRqDto;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.service.exception.ConflictException;
import ru.practicum.ewm.service.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto addNewCategory(CategoryRqDto categoryRqDto) {
        Category category = categoryRepository.save(CategoryMapper.mapToCategory(categoryRqDto));
        return CategoryMapper.mapToDto(category);
    }

    @Override
    public void deleteById(Long id) {
        if (eventRepository.countByCategoryId(id) > 0) {
            throw new ConflictException("Удаление категории невозможно - существуют события, с ней связанные",
                                        "Можно удалять только категории, на которые не существует ссылок");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> findAll(Integer start, Integer size) {
        int pageNumber = size != 0 ? start / size : 0;
        PageRequest pageable = PageRequest.of(pageNumber, size, Sort.by(Category.Fields.id).ascending());
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
