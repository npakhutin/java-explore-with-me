package ru.practicum.ewm.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRqDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> findByAll(@RequestParam(name = "from", defaultValue = "0") Integer start,
                                       @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return categoryService.findAll(start, size);
    }

    @GetMapping("/categories/{id}")
    public CategoryDto findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @DeleteMapping("/admin/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @PatchMapping("/admin/categories/{id}")
    public CategoryDto updateById(@PathVariable Long id, @RequestBody @Valid CategoryRqDto categoryRqDto) {
        return categoryService.updateById(id, categoryRqDto);
    }

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addNewCategory(@RequestBody @Valid CategoryRqDto categoryRqDto) {
        return categoryService.addNewCategory(categoryRqDto);
    }
}
