package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;

    /**
     * <h5>Добавление новой подборки (подборка может не содержать событий)</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li></li>
     * </ul>
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addAdminCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.debug("/admin/compilations - POST: addAdminCompilation({})", newCompilationDto);
        return compilationService.addAdminCompilation(newCompilationDto);
    }

    /**
     * <h5>Удаление подборки</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li></li>
     * </ul>
     */
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdminCompilation(@PathVariable long compId) {
        log.debug("/admin/compilations/{} - DELETE: deleteAdminCategory({})", compId, compId);
        compilationService.deleteAdminCompilation(compId);
    }

    /**
     * <h5>Обновить информацию о подборке</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li></li>
     * </ul>
     */
    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateAdminCompilation(@PathVariable long compId,
                                                 @RequestBody @Valid UpdateCompilationRequest updateRequest) {
        log.debug("/admin/compilations/{} - PATCH: updateAdminCompilation({}, {})", compId, compId, updateRequest);
        return compilationService.updateAdminCompilation(compId, updateRequest);
    }
}
