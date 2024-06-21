package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.UserStateAction;
import ru.practicum.location.dto.LocationDto;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Класс, используемый для пользовательского изменения данных сущности-событие - {@link ru.practicum.event.model.Event}
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class UpdateEventUserRequest {

    private Long category;
    private LocationDto location;

    @Size(min = 3, max = 120, message = "Event.title - Минимальная длина заголовка события - {min}, а максимальная {max} символов")
    private String title;

    @Size(min = 20, max = 2000, message = "Event.annotation - Минимальная длина краткого описания события - {min}, а максимальная {max} символов")
    private String annotation;

    @Size(min = 20, max = 7000, message = "Event.title - Минимальная длина полного описания события - {min}, а максимальная {max} символов")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Boolean requestModeration;
    private Integer participantLimit;
    private Boolean paid;
    private UserStateAction stateAction;
}
