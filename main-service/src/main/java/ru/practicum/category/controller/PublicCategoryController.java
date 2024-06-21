package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class PublicCategoryController {
    private final CategoryService categoryService;

    /**
     * <h5>Получение категорий</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li>в случае, если по заданным фильтрам не найдено ни одной категории, возвращает пустой список</li>
     * </ul>
     */
    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategoryById(@PathVariable long catId) {
        log.debug("/categories/{} - GET: getCategories({})", catId, catId);
        return categoryService.getCategoryById(catId);
    }

    /**
     * <h5>Получение информации о категории по её идентификатору</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li>в случае, если категории с заданным id не найдено, возвращает статус код 404</li>
     * </ul>
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                           @RequestParam(defaultValue = "10") @Positive final int size) {
        log.debug("/categories - GET: getCategories({}, {})", from, size);
        return categoryService.getCategories(from, size);
    }
}
