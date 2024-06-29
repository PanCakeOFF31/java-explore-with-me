package ru.practicum.like.service;

import ru.practicum.like.exception.DislikeAlreadyExistsException;
import ru.practicum.like.exception.LikeAlreadyExistsException;
import ru.practicum.like.exception.NoDislikeToUndoExistsException;
import ru.practicum.like.exception.NoLikeToUndoExistsException;

public interface LikeService {
    void likeEvent(long userId, long eventId) throws LikeAlreadyExistsException;

    void undoLikeEvent(long userId, long eventId) throws NoLikeToUndoExistsException;

    void dislikeEvent(long userId, long eventId) throws DislikeAlreadyExistsException;

    void undoLDislikeEvent(long userId, long eventId) throws NoDislikeToUndoExistsException;
}
