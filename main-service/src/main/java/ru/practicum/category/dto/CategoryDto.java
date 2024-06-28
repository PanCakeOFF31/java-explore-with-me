package ru.practicum.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс, используемый для представления данных сущности-категория - {@link ru.practicum.category.model.Category}
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class CategoryDto {
    private long id;
    private String name;
}
