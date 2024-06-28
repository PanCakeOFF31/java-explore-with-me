package ru.practicum.common.component;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.category.exception.CategoryNotFoundException;
import ru.practicum.category.model.Category;
import ru.practicum.common.exception.InvalidDateTimeRangeException;
import ru.practicum.compilation.exception.CompilationNotFoundException;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.exception.EventNotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.like.exception.LikeNotFoundException;
import ru.practicum.like.model.Like;
import ru.practicum.like.model.LikeId;
import ru.practicum.location.exception.LocationNotFoundException;
import ru.practicum.location.model.Location;
import ru.practicum.partrequest.exception.ParticipationRequestNotFoundException;
import ru.practicum.partrequest.model.ParticipationRequest;
import ru.practicum.rating.exception.RatingNotFoundException;
import ru.practicum.rating.model.Rating;
import ru.practicum.rating.model.RatingId;
import ru.practicum.user.exception.UserNotFoundException;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public interface CommonComponent {
    String prepareMessage(String message, long entityId);

    String prepareMessage(String message, long entityId, long otherEntityId);

    String prepareMessage(String message, String value);

    <T extends RuntimeException> void throwAndLog(Supplier<T> exceptionSupplier);

    // User
    boolean containsUserById(long userId);

    void userExists(long userId) throws UserNotFoundException;

    User getUserById(long userId) throws UserNotFoundException;

    // Category
    boolean containsCategoryById(long categoryId);

    void categoryExists(long categoryId) throws CategoryNotFoundException;

    Category getCategoryById(long categoryId) throws CategoryNotFoundException;

    // Location
    boolean containsLocationByLatAndLon(float lat, float lon);

    void locationExists(float lat, float lon) throws LocationNotFoundException;

    Location getLocationByLatAndLon(float lat, float lon) throws LocationNotFoundException;

    // Event
    boolean containsEventById(long eventId);

    boolean containsPublishedEventById(long eventId);

    void eventExists(long eventId) throws EventNotFoundException;

    void publishedEventExists(long eventId) throws EventNotFoundException;

    Event getEventById(long eventId) throws EventNotFoundException;

    Event getPublishedEventById(long eventId) throws EventNotFoundException;

    // Compilation
    boolean containsCompilationById(long compilationId);

    void compilationExists(long compilationId) throws CompilationNotFoundException;

    Compilation getCompilationById(long compilationId) throws CompilationNotFoundException;

    // ParticipationRequest
    boolean containsParticipationRequestById(long partrequestId);

    void partrequestExists(long partrequestId) throws ParticipationRequestNotFoundException;

    ParticipationRequest getParticipationRequestById(long partrequestId) throws ParticipationRequestNotFoundException;

    // Like
    boolean containsLikeById(LikeId likeId);

    boolean containsLikeById(long userId, long eventId);

    void likeExists(LikeId likeId) throws LikeNotFoundException;

    void likeExists(long userId, long eventId) throws LikeNotFoundException;

    Like getLikeById(LikeId likeId) throws LikeNotFoundException;

    Like getLikeById(long userId, long eventId) throws LikeNotFoundException;

    // Rating
    boolean containsRatingById(RatingId ratingId);

    boolean containsRatingById(long userId, long eventId);

    void ratingExists(RatingId ratingId) throws RatingNotFoundException;

    void ratingExists(long userId, long eventId) throws RatingNotFoundException;

    Rating getRatingById(RatingId ratingId) throws RatingNotFoundException;

    Rating getRatingById(long userId, long eventId) throws RatingNotFoundException;

    // Rating

    // Pageable
    Pageable definePageable(int from, int size);

    Pageable definePageable(int from, int size, Sort sort);

    // DateTime validation
    void dateTimeRangeValidation(LocalDateTime timePoint1, LocalDateTime timePoint2) throws InvalidDateTimeRangeException;
}
