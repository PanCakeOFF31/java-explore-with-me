package ru.practicum.category.dto;

import ru.practicum.category.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {
    public static CategoryDto mapToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static List<CategoryDto> mapToCategoryDto(Iterable<Category> categories) {
        List<CategoryDto> dtos = new ArrayList<>();

        for (Category category : categories) {
            dtos.add(mapToCategoryDto(category));
        }

        return dtos;
    }

    public static Category mapToCategory(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }
}
