package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

/**
 * Класс, используемый для краткого представления данных сущности-событие - {@link ru.practicum.event.model.Event}
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class EventShortDto {
    private long id;
    private CategoryDto category;
    private UserShortDto initiator;
    private String title;
    private String annotation;
    private String eventDate;
    private boolean paid;
    private long views;
    private int confirmedRequests;
    private long likes;
    private long dislikes;
    private float rating;
}
