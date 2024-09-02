package ru.practicum.ewm.compilation;

import ru.practicum.ewm.compilation.dto.AddCompilationRqDto;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRqDto;

import java.util.List;

public interface CompilationService {
    CompilationDto addNewCompilation(AddCompilationRqDto addCompilationRqDto);

    List<CompilationDto> findCompilations(Boolean pinned, Integer start, Integer size);

    CompilationDto findById(Long id);

    CompilationDto updateById(Long id, UpdateCompilationRqDto updateCompilationRqDto);

    void deleteById(Long id);
}
