package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.exception.CompilationFieldOccupiedException;

import java.util.List;

public interface CompilationService {
    CompilationDto addAdminCompilation(NewCompilationDto newCompilationDto) throws CompilationFieldOccupiedException;

    void deleteAdminCompilation(long compId);

    CompilationDto updateAdminCompilation(long compId, UpdateCompilationRequest updateRequest) throws CompilationFieldOccupiedException;

    List<CompilationDto> fetchPublicCompilations(boolean pinned, int from, int size);

    CompilationDto fetchPublicCompilation(long compId);
}
