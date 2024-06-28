package ru.practicum.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Класс, используемый для создания сущности-категория - {@link ru.practicum.category.model.Category}
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class NewCategoryDto {
    @NotBlank(message = "Название категории Category.name не может быть null/empty/blank")
    @Size(min = 1, max = 50, message = "Category.name - Минимальная длина имени категории - {min}, а максимальная {max} символов")
    private String name;
}
