package ru.practicum.rating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.rating.model.Rating;
import ru.practicum.rating.model.RatingId;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {

    @Query("select sum(r.rating)/cast(count(r.userId) as float) " +
            "from Rating as r " +
            "left join Event as e on r.eventId = e.id " +
            "left join ParticipationRequest as pr on e.id = pr.event.id " +
            "where r.eventId = ?1 and pr.status = 'CONFIRMED' " +
            "group by r.eventId ")
    Optional<Float> calculateEventRating(long eventId);
}
