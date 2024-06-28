package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.exception.CategoryFieldOccupiedException;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto newCategoryDto) throws CategoryFieldOccupiedException;

    CategoryDto updateCategory(long catId, NewCategoryDto categoryDto) throws CategoryFieldOccupiedException;

    void deleteCategory(long catId);

    CategoryDto getCategoryById(long catId);

    List<CategoryDto> getCategories(int from, int size);
}
