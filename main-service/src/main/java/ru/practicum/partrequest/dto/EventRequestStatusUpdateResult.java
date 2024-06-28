package ru.practicum.partrequest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Класс, используемый для представления измененных данных сущности-запрос-участия - {@link ru.practicum.partrequest.model.ParticipationRequest}
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
