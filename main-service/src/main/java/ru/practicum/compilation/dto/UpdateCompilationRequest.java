package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Класс, используемый для обновления сущности-подборка - {@link ru.practicum.compilation.model.Compilation}
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class UpdateCompilationRequest {

    @Size(min = 1, max = 50, message = "Compilation.title - Минимальная длина заголовка подборки - {min}, а максимальная {max} символов")
    private String title;
    private Boolean pinned;
    private Set<Long> events;
}
