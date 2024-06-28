package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.dto.EventShortDto;

import java.util.List;

/**
 * Класс, используемый для представления данных сущности-подборка - {@link ru.practicum.compilation.model.Compilation}
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class CompilationDto {
    private long id;
    private String title;
    private List<EventShortDto> events;
    private boolean pinned;
}
