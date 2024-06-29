package ru.practicum.like.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class LikeId implements Serializable {
    private Long userId;
    private Long eventId;
}
