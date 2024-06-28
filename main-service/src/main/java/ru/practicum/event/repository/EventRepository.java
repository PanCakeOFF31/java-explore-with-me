package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    List<Event> findAllByInitiatorId(long initiatorId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(long itemId, long initiatorId);

    Optional<Event> findByIdAndInitiatorIdAndState(long itemId, long initiatorId, EventState eventState);

    boolean existsByIdAndState(long eventId, EventState state);

    Optional<Event> findByIdAndState(long eventId, EventState state);

    @Modifying
    @Query("update Event as e set e.confirmedRequests = ?2 where e.id = ?1")
    void updateEventConfirmedRequestsValue(long eventId, int confirmedRequests);

    @Modifying
    @Query("update Event as e set e.confirmedRequests = e.confirmedRequests + 1 where e.id = ?1")
    void incrementEventConfirmedRequestsValue(long eventId);

    @Modifying
    @Query("update Event as e set e.confirmedRequests = e.confirmedRequests - 1 where e.id = ?1")
    void decrementEventConfirmedRequestsValue(long eventId);

    @Modifying
    @Query("update Event as e set e.likes = e.likes + 1 where e.id = ?1")
    void incrementEventLikesValue(long eventId);

    @Modifying
    @Query("update Event as e set e.likes = e.likes - 1 where e.id = ?1")
    void decrementEventLikesValue(long eventId);

    @Modifying
    @Query("update Event as e set e.dislikes = e.dislikes + 1 where e.id = ?1")
    void incrementEventDislikesValue(long eventId);

    @Modifying
    @Query("update Event as e set e.dislikes = e.dislikes - 1 where e.id = ?1")
    void decrementEventDislikesValue(long eventId);

    @Modifying
    @Query("update Event as e set e.rating = ?2 where e.id = ?1")
    void updateEventRatingValue(long eventId, float rating);
}
