package ru.practicum.like.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@IdClass(LikeId.class)
@Builder(toBuilder = true)
@Table(name = "event_like")
public class Like {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "is_like")
    private Boolean like;
}
