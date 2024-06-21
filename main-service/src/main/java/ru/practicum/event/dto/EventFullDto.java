package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.model.EventState;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.dto.UserShortDto;

/**
 * Класс, используемый для полного представления данных сущности-событие - {@link ru.practicum.event.model.Event}
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class EventFullDto {
    private long id;
    private CategoryDto category;
    private UserShortDto initiator;
    private LocationDto location;
    private String title;
    private String annotation;
    private String description;
    private String createdOn;
    private String eventDate;
    private String publishedOn;
    private int participantLimit;
    private boolean requestModeration;
    private boolean paid;
    private EventState state;
    private long views;
    private int confirmedRequests;
}
