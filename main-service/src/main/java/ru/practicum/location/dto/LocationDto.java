package ru.practicum.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Сущность, используемая для всех сценариев сущности-пользователь - {@link ru.practicum.location.model.Location}
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class LocationDto {
    @NotNull
    private Float lat;
    @NotNull
    private Float lon;
}
