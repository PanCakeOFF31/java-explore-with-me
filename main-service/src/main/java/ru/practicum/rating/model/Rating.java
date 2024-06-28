package ru.practicum.rating.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@IdClass(RatingId.class)
@Builder(toBuilder = true)
@Table(name = "event_rating")
public class Rating {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Column
    private Integer rating;
}
