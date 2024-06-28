package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;

    /**
     * <h5>Добавление новой категории</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li>имя категории должно быть уникальны</li>
     * </ul>
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.debug("/admin/categories - POST: createCategory({})", newCategoryDto);
        return categoryService.createCategory(newCategoryDto);
    }

    /**
     * <h5>Изменение категории</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li>имя категории должно быть уникальным</li>
     * </ul>
     */
    // Чтобы не создавать новую сущность Dto используется NewCategoryDto
    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable long catId,
                                      @RequestBody @Valid NewCategoryDto categoryDto) {
        log.debug("/admin/categories/{} - PATCH: updateCategory({})", catId, categoryDto);
        return categoryService.updateCategory(catId, categoryDto);
    }


    /**
     * <h5>Удаление категории</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li>с категорией не должно быть связано ни одного события.</li>
     * </ul>
     */
    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        log.debug("/admin/users/{} - DELETED: deleteCategory({})", catId, catId);
        categoryService.deleteCategory(catId);
    }
}

