package ru.practicum.partrequest.dto;

import ru.practicum.partrequest.model.ParticipationRequest;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.common.component.CommonComponentImpl.DTF;

public class ParticipationRequestMapper {

    public static ParticipationRequestDto mapToParticipationRequestDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .created(DTF.format(request.getCreated()))
                .status(request.getStatus())
                .build();
    }

    public static List<ParticipationRequestDto> mapToParticipationRequestDto(Iterable<ParticipationRequest> requests) {
        List<ParticipationRequestDto> dtos = new ArrayList<>();

        for (ParticipationRequest request : requests) {
            dtos.add(mapToParticipationRequestDto(request));
        }

        return dtos;
    }
}

