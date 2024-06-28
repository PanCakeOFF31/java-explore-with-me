package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.component.CommonComponent;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.exception.CompilationFieldOccupiedException;
import ru.practicum.compilation.exception.CompilationNotFoundException;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CommonComponent commonComponent;
    private final CompilationRepository compilationRepository;
    private final EventService eventService;

    private static final String COMPILATION_TITLE_OCCUPIED = "Потенциальное нарушение целостности данных. Поле Compilation.title: '%s' уже занято.";

    @Override
    @Transactional
    public CompilationDto addAdminCompilation(NewCompilationDto newCompilationDto) throws CompilationFieldOccupiedException {
        log.debug("CompilationServiceImpl - service.addAdminCompilation({})", newCompilationDto);

        toAddAdminCompilationValidation(newCompilationDto.getTitle());
        Compilation compilationToSave = CompilationMapper.mapToCompilation(newCompilationDto);

        List<Event> events = eventService.getEventsByIds(newCompilationDto.getEvents());
        compilationToSave.setEvents(events);

        return CompilationMapper.mapToCompilationDto(compilationRepository.save(compilationToSave));
    }

    private void toAddAdminCompilationValidation(String title) {
        log.debug("CompilationServiceImpl - service.toAddAdminCompilationValidation({})", title);

        Optional<Compilation> foundCompilation = compilationRepository.findCompilationByTitle(title);

        if (foundCompilation.isPresent())
            commonComponent.throwAndLog(() -> new CompilationNotFoundException(commonComponent
                    .prepareMessage(COMPILATION_TITLE_OCCUPIED, title)));
    }

    @Override
    @Transactional
    public void deleteAdminCompilation(long compId) {
        log.debug("CompilationServiceImpl - service.deleteAdminCompilation({})", compId);
        commonComponent.compilationExists(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateAdminCompilation(long compId, UpdateCompilationRequest updateRequest) throws CompilationFieldOccupiedException {
        log.debug("CompilationServiceImpl - service.updateAdminCompilation({})", compId);

        Compilation updateCompilation = CompilationMapper.mapToCompilation(updateRequest);

        toUpdateAdminCompilationValidation(compId, updateRequest.getTitle());

        Compilation compilationToUpdate = commonComponent.getCompilationById(compId);

        if (updateCompilation.getTitle() != null)
            compilationToUpdate.setTitle(updateCompilation.getTitle());

        if (updateCompilation.getPinned() != null)
            compilationToUpdate.setPinned(updateCompilation.getPinned());

        // Нет подходящего поля при mapping, напрямую обращаемся
        if (updateRequest.getEvents() != null) {
            List<Event> events = eventService.getEventsByIds(updateRequest.getEvents());
            compilationToUpdate.setEvents(events);
        }

        return CompilationMapper.mapToCompilationDto(compilationRepository.save(compilationToUpdate));
    }


    private void toUpdateAdminCompilationValidation(long compId, String title) {
        log.debug("CompilationServiceImpl - service.toUpdateAdminCompilation({}, {})", compId, title);

        if (title == null)
            return;

        Optional<Compilation> foundCompilation = compilationRepository.findCompilationByTitle(title);

        if (foundCompilation.isPresent() && foundCompilation.get().getId() != compId)
            commonComponent.throwAndLog(() -> new CompilationNotFoundException(commonComponent
                    .prepareMessage(COMPILATION_TITLE_OCCUPIED, title)));
    }

    @Override
    public List<CompilationDto> fetchPublicCompilations(boolean pinned, int from, int size) {
        log.debug("CompilationServiceImpl - service.fetchPublicCompilations({}, {}, {})", pinned, from, size);
        Pageable pageable = commonComponent.definePageable(from, size, Sort.by(Sort.Order.asc("id")));
        return CompilationMapper.mapToCompilationDto(compilationRepository.findAllByPinned(pinned, pageable));
    }

    @Override
    public CompilationDto fetchPublicCompilation(long compId) {
        log.debug("CompilationServiceImpl - service.fetchPublicCompilation({})", compId);
        Compilation compilation = commonComponent.getCompilationById(compId);
        return CompilationMapper.mapToCompilationDto(compilation);
    }
}
