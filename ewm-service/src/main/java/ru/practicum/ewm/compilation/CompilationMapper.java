package ru.practicum.ewm.compilation;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation mapToCompilation(String title, Boolean pinned, List<Event> events) {
        return new Compilation(null, title, pinned, events);
    }

    public static CompilationDto mapToDto(Compilation compilation) {
        return new CompilationDto(compilation.getId(),
                                  compilation.getEvents()
                                             .stream()
                                             .map(EventMapper::mapToShortDto)
                                             .collect(Collectors.toList()),
                                  compilation.getPinned(),
                                  compilation.getTitle());
    }
}
