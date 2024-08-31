package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.AddCompilationRqDto;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRqDto;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.service.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto addNewCompilation(AddCompilationRqDto addCompilationRqDto) {
        List<Event> events;
        if (!addCompilationRqDto.getEvents().isEmpty()) {
            events = eventRepository.findAllById(addCompilationRqDto.getEvents());
        } else {
            events = new ArrayList<>();
        }
        Compilation compilation = compilationRepository.save(CompilationMapper.mapToCompilation(addCompilationRqDto.getTitle(),
                                                                                                Optional.ofNullable(
                                                                                                                addCompilationRqDto.getPinned())
                                                                                                        .orElse(false),
                                                                                                events));
        return CompilationMapper.mapToDto(compilation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> findAll(Integer start, Integer size) {
        PageRequest pageable = PageRequest.of(start, size, Sort.by(Compilation.Fields.id).ascending());
        return compilationRepository.findAll(pageable)
                                    .stream()
                                    .map(CompilationMapper::mapToDto)
                                    .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto findById(Long id) {
        return CompilationMapper.mapToDto(compilationRepository.findById(id)
                                                               .orElseThrow(() -> new NotFoundException(
                                                                       "Не найдена подборка id = " + id)));
    }

    @Override
    public CompilationDto updateById(Long id, UpdateCompilationRqDto updateCompilationRqDto) {
        Compilation compilation = compilationRepository.findById(id)
                                                       .orElseThrow(() -> new NotFoundException(
                                                               "Не найдена подборка id = " + id));
        List<Event> events;
        if (!updateCompilationRqDto.getEvents().isEmpty()) {
            events = eventRepository.findAllById(updateCompilationRqDto.getEvents());
        } else {
            events = new ArrayList<>();
        }

        if (updateCompilationRqDto.getTitle() != null) {
            compilation.setTitle(updateCompilationRqDto.getTitle());
        }
        if (updateCompilationRqDto.getPinned() != null) {
            compilation.setPinned(updateCompilationRqDto.getPinned());
        }

        compilation.getEvents().clear();
        compilation.getEvents().addAll(events);

        return CompilationMapper.mapToDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteById(Long id) {
        compilationRepository.deleteById(id);
    }
}
