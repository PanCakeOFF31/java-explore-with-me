package ru.practicum.partrequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.exception.WrongEventStateException;
import ru.practicum.partrequest.exception.ParticRequestWithRequesterAndEventNotFoundException;
import ru.practicum.partrequest.exception.ParticipationLimitException;
import ru.practicum.partrequest.exception.RequesterIsEventInitiatorException;
import ru.practicum.partrequest.exception.SameRequestExistsException;
import ru.practicum.partrequest.model.ParticipationRequest;
import ru.practicum.partrequest.model.RequestStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    Optional<ParticipationRequest> findByRequesterIdAndEventId(long requesterId, long eventId) throws WrongEventStateException,
            RequesterIsEventInitiatorException,
            SameRequestExistsException,
            ParticipationLimitException;

    Optional<ParticipationRequest> findByIdAndRequesterId(long requestId, long requesterId) throws ParticRequestWithRequesterAndEventNotFoundException;


    List<ParticipationRequest> findAllByRequesterId(long requesterId);

    List<ParticipationRequest> findAllByEventIdAndIdIn(long eventId, Set<Long> ids);

    Optional<ParticipationRequest> findByRequesterIdAndEventIdAndStatus(long userId, long eventId, RequestStatus status);
}
