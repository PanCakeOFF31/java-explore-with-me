package ru.practicum.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.like.model.Like;
import ru.practicum.like.model.LikeId;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
}
