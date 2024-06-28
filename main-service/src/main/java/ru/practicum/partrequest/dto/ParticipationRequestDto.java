package ru.practicum.partrequest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.partrequest.model.RequestStatus;

/**
 * Класс, используемый для представления данных сущности-запрос-участия - {@link ru.practicum.partrequest.model.ParticipationRequest}
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class ParticipationRequestDto {
    private long id;
    private long requester;
    private long event;
    private String created;
    private RequestStatus status;
}
