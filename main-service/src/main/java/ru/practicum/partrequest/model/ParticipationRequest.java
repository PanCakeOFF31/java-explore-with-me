package ru.practicum.partrequest.model;

import lombok.*;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Dto-классы - {@link ru.practicum.partrequest.dto}
 * <ul>
 *     <li>Представление данных - {@link ru.practicum.partrequest.dto.ParticipationRequestDto}</li>
 *     <li >Изменение данных - {@link ru.practicum.partrequest.dto.EventRequestStatusUpdateRequest}</li>
 *     <li >Представление измененных данных - {@link ru.practicum.partrequest.dto.EventRequestStatusUpdateResult}</li>
 * </ul>
 * <p>Создание класса происходит через @PathVariable для userId и @RequestParam для eventId</p>
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "request")
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_on")
    private LocalDateTime created;

    @Column(length = 9)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @ToString.Exclude
    @JoinColumn(name = "requester_id")
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    private User requester;

    @ToString.Exclude
    @JoinColumn(name = "event_id")
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Event.class)
    private Event event;
}
