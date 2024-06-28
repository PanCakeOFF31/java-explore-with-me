package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.location.dto.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Класс, используемый для создания сущности-событие - {@link ru.practicum.event.model.Event}
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class NewEventDto {

    @NotNull(message = "Категория события Event.category не может быть null")
    private Long category;

    @NotNull(message = "Местоположение события Event.location не может быть null")
    private LocationDto location;

    @NotBlank(message = "Заголовок события Event.title не может быть null/empty/blank")
    @Size(min = 3, max = 120, message = "Event.title - Минимальная длина заголовка события - {min}, а максимальная {max} символов")
    private String title;

    @NotBlank(message = "Краткое описание события Event.annotation не может быть null/empty/blank")
    @Size(min = 20, max = 2000, message = "Event.annotation - Минимальная длина краткого описания события - {min}, а максимальная {max} символов")
    private String annotation;

    @NotBlank(message = "Полное описание события Event.description не может быть null/empty/blank")
    @Size(min = 20, max = 7000, message = "Event.description - Минимальная длина полного описания события - {min}, а максимальная {max} символов")
    private String description;

    @NotNull(message = "Дата события Event.eventDate не может быть null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    // Необязательные поля в Dto, которые имеют значение по умолчанию.
    private Boolean paid = false;

    @PositiveOrZero
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
}
