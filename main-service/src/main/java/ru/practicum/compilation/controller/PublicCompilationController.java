package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class PublicCompilationController {
    private final CompilationService compilationService;


    /**
     * <h5>Получение подборок событий</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li>в случае, если по заданным фильтрам не найдено ни одной подборки, возвращает пустой список</li>
     * </ul>
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> fetchPublicCompilations(@RequestParam(defaultValue = "false") boolean pinned,
                                                        @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                                        @RequestParam(defaultValue = "10") @Positive final int size) {
        log.debug("/compilations - GET: fetchPublicCompilations({}, {}, {})", pinned, from, size);
        return compilationService.fetchPublicCompilations(pinned, from, size);
    }

    /**
     * <h5>Получение подборки событий по id</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li>в случае, если подборки с заданным id не найдено, возвращает статус код 404</li>
     * </ul>
     */
    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto fetchPublicCompilation(@PathVariable long compId) {
        log.debug("/compilations/{} - GET: fetchPublicCompilation({})", compId, compId);
        return compilationService.fetchPublicCompilation(compId);
    }
}
