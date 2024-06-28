package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.compilation.model.Compilation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;

/**
 * Класс, используемый для создания сущности-подборка - {@link Compilation}
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class NewCompilationDto {

    @NotBlank(message = "Заголовок подборки Compilation.name не может быть null/empty/blank")
    @Size(min = 1, max = 50, message = "Compilation.title - Минимальная длина заголовка подборки - {min}, а максимальная {max} символов")
    private String title;

    private HashSet<Long> events;

    // Необязательные поля в Dto, которые имеют значение по умолчанию
    private boolean pinned = false;
}
