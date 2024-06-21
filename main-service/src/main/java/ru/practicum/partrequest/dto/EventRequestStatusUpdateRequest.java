package ru.practicum.partrequest.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.partrequest.model.UpdateStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Класс, используемый для представления изменения данных сущности-запрос-участия - {@link ru.practicum.partrequest.model.ParticipationRequest}
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class EventRequestStatusUpdateRequest {

    @NotNull
    @Size(min = 1, message = "Как минимум один запрос должен быть указан при обновлении")
    private Set<Long> requestIds;

    @NotNull
    private UpdateStatus status;
}
