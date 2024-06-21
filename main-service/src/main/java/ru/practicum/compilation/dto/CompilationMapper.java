package ru.practicum.compilation.dto;

import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventMapper;

import java.util.ArrayList;
import java.util.List;

public class CompilationMapper {
    public static Compilation mapToCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.isPinned())
                .build();
    }

    public static Compilation mapToCompilation(UpdateCompilationRequest updateRequest) {
        return Compilation.builder()
                .title(updateRequest.getTitle())
                .pinned(updateRequest.getPinned())
                .build();
    }

    public static CompilationDto mapToCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(EventMapper.mapToEventShortDto(compilation.getEvents()))
                .build();
    }

    public static List<CompilationDto> mapToCompilationDto(Iterable<Compilation> compilations) {
        List<CompilationDto> dtos = new ArrayList<>();

        for (Compilation compilation : compilations) {
            dtos.add(mapToCompilationDto(compilation));
        }

        return dtos;
    }
}
