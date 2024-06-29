package ru.practicum.common.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.category.exception.CategoryNotFoundException;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.common.exception.InvalidDateTimeRangeException;
import ru.practicum.compilation.exception.CompilationNotFoundException;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.exception.EventNotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.like.exception.LikeNotFoundException;
import ru.practicum.like.model.Like;
import ru.practicum.like.model.LikeId;
import ru.practicum.like.repository.LikeRepository;
import ru.practicum.location.exception.LocationNotFoundException;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.partrequest.exception.ParticipationRequestNotFoundException;
import ru.practicum.partrequest.model.ParticipationRequest;
import ru.practicum.partrequest.repository.ParticipationRequestRepository;
import ru.practicum.rating.exception.RatingNotFoundException;
import ru.practicum.rating.model.Rating;
import ru.practicum.rating.model.RatingId;
import ru.practicum.rating.repository.RatingRepository;
import ru.practicum.user.exception.UserNotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

/**
 * Используется для избежания циклических зависимостей
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CommonComponentImpl implements CommonComponent {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final LikeRepository likeRepository;
    private final RatingRepository ratingRepository;

    public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String USER_NOT_FOUND = "User with id=%d was not found.";
    private static final String CATEGORY_NOT_FOUND = "Category with id=%d was not found.";
    private static final String LOCATION_NOT_FOUND = "Location with latitude=%f and longitude=%f was not found.";
    private static final String EVENT_NOT_FOUND = "Event with id=%d was not found.";
    private static final String PUBLISHED_EVENT_NOT_FOUND = "Published event with id=%d was not found.";
    private static final String COMPILATION_NOT_FOUND = "Compilation with id=%d was not found.";
    private static final String PARTICIPATION_REQUEST_NOT_FOUND = "Request with id=%d was not found.";
    private static final String LIKE_NOT_FOUND = "LIKE with id=(%d, %d) was not found.";
    private static final String RATING_NOT_FOUND = "Rating with User.id=%d and Event.id=%d was not found.";
    private static final String INVALID_DATE_TIME_RANGE = "DateTime range problems. %s must be after %s.";

    public static String formatDateTime(LocalDateTime ldt) {
        log.debug("CommonComponentImpl - component.formatDateTime({})", ldt);
        return DTF.format(ldt);
    }

    public String prepareMessage(String message, long entityId) {
        return String.format(message, entityId);
    }

    public String prepareMessage(String message, long entityId, long otherEntityId) {
        return String.format(message, entityId, otherEntityId);
    }

    public String prepareMessage(String message, String value) {
        return String.format(message, value);
    }

    @Override
    public <T extends RuntimeException> void throwAndLog(Supplier<T> exceptionSupplier) {
        log.info(exceptionSupplier.get().getMessage());
        throw exceptionSupplier.get();
    }

    private String prepareMessage(String message, float lat, float lon) {
        return String.format(message, lat, lon);
    }

    @Override
    public boolean containsUserById(long userId) {
        log.debug("CommonComponentImpl - component.containsUserById({})", userId);
        return userRepository.existsById(userId);
    }

    @Override
    public void userExists(long userId) throws UserNotFoundException {
        log.debug("CommonComponentImpl - component.userExists({})", userId);
        if (!containsUserById(userId))
            throwAndLog(() -> new UserNotFoundException(prepareMessage(USER_NOT_FOUND, userId)));
    }

    @Override
    public User getUserById(long userId) throws UserNotFoundException {
        log.debug("CommonComponentImpl - component.getUserById({})", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    String message = prepareMessage(USER_NOT_FOUND, userId);
                    log.info(message);
                    return new UserNotFoundException(message);
                });
    }

    @Override
    public boolean containsCategoryById(long categoryId) {
        log.debug("CommonComponentImpl - component.containsCategoryById({})", categoryId);
        return categoryRepository.existsById(categoryId);
    }

    @Override
    public void categoryExists(long categoryId) throws CategoryNotFoundException {
        log.debug("CommonComponentImpl - component.categoryExists({})", categoryId);

        if (!containsCategoryById(categoryId))
            throwAndLog(() -> new CategoryNotFoundException(prepareMessage(CATEGORY_NOT_FOUND, categoryId)));
    }

    @Override
    public Category getCategoryById(long categoryId) throws CategoryNotFoundException {
        log.debug("CommonComponentImpl - component.getCategoryById({})", categoryId);
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    String message = prepareMessage(CATEGORY_NOT_FOUND, categoryId);
                    log.info(message);
                    return new CategoryNotFoundException(message);
                });
    }

    @Override
    public boolean containsLocationByLatAndLon(float lat, float lon) {
        log.debug("CommonComponentImpl - component.containsLocationById({}, {})", lat, lon);
        return locationRepository.existsByLatAndLon(lat, lon);
    }

    @Override
    public void locationExists(float lat, float lon) throws LocationNotFoundException {
        log.debug("CommonComponentImpl - component.locationExists({}, {})", lat, lon);
        if (!containsLocationByLatAndLon(lat, lon))
            throwAndLog(() -> new LocationNotFoundException(prepareMessage(LOCATION_NOT_FOUND, lat, lon)));
    }

    @Override
    public Location getLocationByLatAndLon(float lat, float lon) throws LocationNotFoundException {
        log.debug("CommonComponentImpl - component.getLocationById({}, {})", lat, lon);
        return locationRepository.findByLatAndLon(lat, lon)
                .orElseThrow(() -> {
                    String message = prepareMessage(LOCATION_NOT_FOUND, lat, lon);
                    log.info(message);
                    return new LocationNotFoundException(message);
                });
    }

    @Override
    public boolean containsEventById(long eventId) {
        log.debug("CommonComponentImpl - component.containsEventById({})", eventId);
        return eventRepository.existsById(eventId);
    }

    @Override
    public boolean containsPublishedEventById(long eventId) {
        log.debug("CommonComponentImpl - component.containsPublishedEventById({})", eventId);
        return eventRepository.existsByIdAndState(eventId, EventState.PUBLISHED);
    }

    @Override
    public void eventExists(long eventId) throws EventNotFoundException {
        log.debug("CommonComponentImpl - component.eventExists({})", eventId);
        if (!containsEventById(eventId)) {
            throwAndLog(() -> new EventNotFoundException(prepareMessage(EVENT_NOT_FOUND, eventId)));
        }
    }

    @Override
    public void publishedEventExists(long eventId) throws EventNotFoundException {
        log.debug("CommonComponentImpl - component.publishedEventExists({})", eventId);
        if (!containsPublishedEventById(eventId)) {
            throwAndLog(() -> new EventNotFoundException(prepareMessage(PUBLISHED_EVENT_NOT_FOUND, eventId)));
        }
    }

    @Override
    public Event getEventById(long eventId) throws EventNotFoundException {
        log.debug("CommonComponentImpl - component.getEventById({})", eventId);
        return eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    String message = prepareMessage(EVENT_NOT_FOUND, eventId);
                    log.info(message);
                    return new EventNotFoundException(message);
                });
    }

    @Override
    public Event getPublishedEventById(long eventId) throws EventNotFoundException {
        log.debug("CommonComponentImpl - component.getPublishedEventById({})", eventId);
        return eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> {
                    String message = prepareMessage(PUBLISHED_EVENT_NOT_FOUND, eventId);
                    log.info(message);
                    return new EventNotFoundException(message);
                });
    }

    @Override
    public boolean containsCompilationById(long compilationId) {
        log.debug("CommonComponentImpl - component.containsCompilationById({})", compilationId);
        return compilationRepository.existsById(compilationId);
    }

    @Override
    public void compilationExists(long compilationId) throws CompilationNotFoundException {
        log.debug("CommonComponentImpl - component.compilationExists({})", compilationId);
        if (!containsCompilationById(compilationId))
            throwAndLog(() -> new CompilationNotFoundException(prepareMessage(COMPILATION_NOT_FOUND, compilationId)));

    }

    @Override
    public Compilation getCompilationById(long compilationId) throws CompilationNotFoundException {
        log.debug("CommonComponentImpl - component.getCompilationById({})", compilationId);
        return compilationRepository.findById(compilationId)
                .orElseThrow(() -> {
                    String message = prepareMessage(COMPILATION_NOT_FOUND, compilationId);
                    log.info(message);
                    return new CompilationNotFoundException(message);
                });
    }

    @Override
    public boolean containsParticipationRequestById(long partrequestId) {
        log.debug("CommonComponentImpl - component.containsParticipationRequestById({})", partrequestId);
        return participationRequestRepository.existsById(partrequestId);
    }

    @Override
    public void partrequestExists(long partrequestId) throws ParticipationRequestNotFoundException {
        log.debug("CommonComponentImpl - component.partrequestExists({})", partrequestId);
        if (!containsParticipationRequestById(partrequestId))
            throwAndLog(() -> new ParticipationRequestNotFoundException(prepareMessage(PARTICIPATION_REQUEST_NOT_FOUND, partrequestId)));
    }

    @Override
    public ParticipationRequest getParticipationRequestById(long partrequestId) throws
            ParticipationRequestNotFoundException {
        log.debug("CommonComponentImpl - component.getParticipationRequestById({})", partrequestId);
        return participationRequestRepository.findById(partrequestId)
                .orElseThrow(() -> {
                    String message = prepareMessage(PARTICIPATION_REQUEST_NOT_FOUND, partrequestId);
                    log.info(message);
                    return new ParticipationRequestNotFoundException(message);
                });
    }

    @Override
    public boolean containsLikeById(LikeId likeId) {
        log.debug("CommonComponentImpl - component.containsLikeById({})", likeId);
        return likeRepository.existsById(likeId);
    }

    @Override
    public boolean containsLikeById(long userId, long eventId) {
        log.debug("CommonComponentImpl - component.containsLikeById({}, {})", userId, eventId);
        return containsLikeById(LikeId.of(userId, eventId));
    }

    @Override
    public void likeExists(LikeId likeId) throws LikeNotFoundException {
        log.debug("CommonComponentImpl - component.likeExists({})", likeId);
        likeExists(likeId.getUserId(), likeId.getEventId());
    }

    @Override
    public void likeExists(long userId, long eventId) throws LikeNotFoundException {
        log.debug("CommonComponentImpl - component.likeExists({}, {})", userId, eventId);
        if (!containsLikeById(LikeId.of(userId, eventId))) {
            throwAndLog(() -> new LikeNotFoundException(prepareMessage(LIKE_NOT_FOUND, userId, eventId)));
        }
    }

    @Override
    public Like getLikeById(LikeId likeId) throws LikeNotFoundException {
        log.debug("CommonComponentImpl - component.getLikeById({})", likeId);
        return getLikeById(likeId.getUserId(), likeId.getEventId());
    }

    @Override
    public Like getLikeById(long userId, long eventId) throws LikeNotFoundException {
        log.debug("CommonComponentImpl - component.getLikeById({}, {})", userId, eventId);
        return likeRepository.findById(LikeId.of(userId, eventId))
                .orElseThrow(() -> {
                    String message = prepareMessage(LIKE_NOT_FOUND, userId, eventId);
                    log.info(message);
                    return new LikeNotFoundException(message);
                });
    }

    @Override
    public boolean containsRatingById(RatingId ratingId) {
        log.debug("CommonComponentImpl - component.containsRatingById({})", ratingId);
        return ratingRepository.existsById(ratingId);
    }

    @Override
    public boolean containsRatingById(long userId, long ratingId) {
        log.debug("CommonComponentImpl - component.containsRatingById({}, {})", userId, ratingId);
        return containsRatingById(RatingId.of(userId, ratingId));
    }

    @Override
    public void ratingExists(RatingId ratingId) throws RatingNotFoundException {
        log.debug("CommonComponentImpl - component.ratingExists({})", ratingId);
        ratingExists(ratingId.getUserId(), ratingId.getEventId());
    }

    @Override
    public void ratingExists(long userId, long eventId) throws RatingNotFoundException {
        log.debug("CommonComponentImpl - component.ratingExists({}, {})", userId, eventId);
        if (!containsRatingById(RatingId.of(userId, eventId))) {
            throwAndLog(() -> new RatingNotFoundException(prepareMessage(RATING_NOT_FOUND, userId, eventId)));
        }
    }

    @Override
    public Rating getRatingById(RatingId ratingId) throws RatingNotFoundException {
        log.debug("CommonComponentImpl - component.getRatingById({})", ratingId);
        return getRatingById(ratingId.getUserId(), ratingId.getEventId());
    }

    @Override
    public Rating getRatingById(long userId, long eventId) throws RatingNotFoundException {
        log.debug("CommonComponentImpl - component.getRatingById({}, {})", userId, eventId);
        return ratingRepository.findById(RatingId.of(userId, eventId))
                .orElseThrow(() -> {
                    String message = prepareMessage(RATING_NOT_FOUND, userId, eventId);
                    log.info(message);
                    return new RatingNotFoundException(message);
                });
    }

    @Override
    public Pageable definePageable(int from, int size) {
        log.debug("CommonComponentImpl - component.definePageable({}, {})", from, size);
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }

    @Override
    public Pageable definePageable(int from, int size, Sort sort) {
        log.debug("CommonComponentImpl - component.definePageable({}, {}, {})", from, size, sort);
        return PageRequest.of(from > 0 ? from / size : 0, size, sort);
    }

    @Override
    public void dateTimeRangeValidation(LocalDateTime timePoint1, LocalDateTime timePoint2) throws
            InvalidDateTimeRangeException {
        log.debug("CommonComponentImpl - service.dateTimeRangeValidation({}, {})", timePoint1, timePoint2);

        if (timePoint1 == null || timePoint2 == null)
            return;

        if (timePoint2.isBefore(timePoint1))
            throwAndLog(() -> new InvalidDateTimeRangeException(String.format(INVALID_DATE_TIME_RANGE, timePoint2, timePoint1)));
    }
}
