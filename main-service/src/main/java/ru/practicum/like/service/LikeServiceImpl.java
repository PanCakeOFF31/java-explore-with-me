package ru.practicum.like.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.component.CommonComponent;
import ru.practicum.event.service.EventService;
import ru.practicum.like.exception.*;
import ru.practicum.like.model.Like;
import ru.practicum.like.model.LikeId;
import ru.practicum.like.repository.LikeRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeServiceImpl implements LikeService {
    private final CommonComponent commonComponent;
    private final LikeRepository likeRepository;
    private final EventService eventService;

    private static final String LIKE_ALREADY_EXISTS = "Лайк от пользователя %d для события %d уже существует.";
    private static final String NO_LIKE_EXISTS = "Лайк от пользователя %d для события %d, который нужно отменить не существует.";
    private static final String DISLIKE_ALREADY_EXISTS = "Дизлайк от пользователя %d для события %d уже существует.";
    private static final String NO_DISLIKE_EXISTS = "Дизлайк от пользователя %d для события %d, который нужно отменить не существует.";

    @Override
    @Transactional
    public void likeEvent(long userId, long eventId) throws LikeAlreadyExistsException {
        log.debug("LikeServiceImpl - service.likeEvent({}, {})", userId, eventId);

        commonComponent.userExists(userId);
        commonComponent.publishedEventExists(eventId);

        LikeId likeId = LikeId.of(userId, eventId);
        Optional<Like> foundLike = likeRepository.findById(likeId);

        if (foundLike.isPresent()) {
            Like like = foundLike.get();

            if (like.getLike())
                commonComponent.throwAndLog(() -> new LikeAlreadyExistsException(commonComponent
                        .prepareMessage(LIKE_ALREADY_EXISTS, userId, eventId)));

            eventService.decrementEventDislikesValue(eventId);
            eventService.incrementEventLikesValue(eventId);
        } else {
            likeRepository.save(Like.of(userId, eventId, true));
            eventService.incrementEventLikesValue(eventId);
        }
    }

    @Override
    @Transactional
    public void undoLikeEvent(long userId, long eventId) throws LikeNotFoundException, NoLikeToUndoExistsException {
        log.debug("LikeServiceImpl - service.undoLikeEvent({}, {})", userId, eventId);

        commonComponent.userExists(userId);
        commonComponent.publishedEventExists(eventId);

        LikeId likeId = LikeId.of(userId, eventId);
        Optional<Like> foundLike = likeRepository.findById(likeId);

        if (foundLike.isPresent()) {
            Like like = foundLike.get();

            if (!like.getLike()) {
                commonComponent.throwAndLog(() -> new NoLikeToUndoExistsException(commonComponent
                        .prepareMessage(NO_LIKE_EXISTS, userId, eventId)));
            }

            eventService.decrementEventLikesValue(eventId);
        } else {
            commonComponent.throwAndLog(() -> new NoLikeToUndoExistsException(commonComponent
                    .prepareMessage(NO_LIKE_EXISTS, userId, eventId)));
        }
    }

    @Override
    @Transactional
    public void dislikeEvent(long userId, long eventId) throws DislikeAlreadyExistsException {
        log.debug("LikeServiceImpl - service.dislikeEvent({}, {})", userId, eventId);

        commonComponent.userExists(userId);
        commonComponent.publishedEventExists(eventId);

        LikeId likeId = LikeId.of(userId, eventId);
        Optional<Like> foundLike = likeRepository.findById(likeId);

        if (foundLike.isPresent()) {
            Like like = foundLike.get();

            if (!like.getLike())
                commonComponent.throwAndLog(() -> new DislikeAlreadyExistsException(commonComponent
                        .prepareMessage(DISLIKE_ALREADY_EXISTS, userId, eventId)));

            eventService.decrementEventLikesValue(eventId);
            eventService.incrementEventDislikesValue(eventId);
        } else {
            likeRepository.save(Like.of(userId, eventId, false));
            eventService.incrementEventDislikesValue(eventId);
        }
    }

    @Override
    @Transactional
    public void undoLDislikeEvent(long userId, long eventId) throws NoDislikeToUndoExistsException {
        log.debug("LikeServiceImpl - service.undoLDislikeEvent({}, {})", userId, eventId);

        commonComponent.userExists(userId);
        commonComponent.publishedEventExists(eventId);

        LikeId likeId = LikeId.of(userId, eventId);
        Optional<Like> foundLike = likeRepository.findById(likeId);

        if (foundLike.isPresent()) {
            Like like = foundLike.get();

            if (like.getLike()) {
                commonComponent.throwAndLog(() -> new NoDislikeToUndoExistsException(commonComponent
                        .prepareMessage(NO_DISLIKE_EXISTS, userId, eventId)));
            }

            eventService.decrementEventDislikesValue(eventId);
        } else {
            commonComponent.throwAndLog(() -> new NoDislikeToUndoExistsException(commonComponent
                    .prepareMessage(NO_DISLIKE_EXISTS, userId, eventId)));
        }
    }
}