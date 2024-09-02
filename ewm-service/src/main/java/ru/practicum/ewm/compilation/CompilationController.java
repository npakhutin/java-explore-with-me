package ru.practicum.ewm.compilation;

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
import ru.practicum.ewm.compilation.dto.AddCompilationRqDto;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRqDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public List<CompilationDto> findCompilations(@RequestParam(name = "pinned", defaultValue = "") Boolean pinned,
                                                 @RequestParam(name = "from", defaultValue = "0") Integer start,
                                                 @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return compilationService.findCompilations(pinned, start, size);
    }

    @GetMapping("/compilations/{id}")
    public CompilationDto findById(@PathVariable Long id) {
        return compilationService.findById(id);
    }

    @DeleteMapping("/admin/compilations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        compilationService.deleteById(id);
    }

    @PatchMapping("/admin/compilations/{id}")
    public CompilationDto updateById(@PathVariable Long id,
                                     @RequestBody @Valid UpdateCompilationRqDto updateCompilationRqDto) {
        return compilationService.updateById(id, updateCompilationRqDto);
    }

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addNewCompilation(@RequestBody @Valid AddCompilationRqDto addCompilationRqDto) {
        return compilationService.addNewCompilation(addCompilationRqDto);
    }
}
