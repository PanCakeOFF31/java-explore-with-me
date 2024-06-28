package ru.practicum.event.model;

import lombok.*;
import ru.practicum.category.model.Category;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.location.model.Location;
import ru.practicum.partrequest.model.ParticipationRequest;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Dto-классы - {@link ru.practicum.event.dto}
 * <ul>
 *     <li>Создание класса - {@link ru.practicum.event.dto.NewEventDto}</li>
 *     <li >Краткая форма представления - {@link ru.practicum.event.dto.EventShortDto}</li>
 *     <li >Полная форма представления - {@link ru.practicum.event.dto.EventFullDto}</li>
 *     <li>Административное изменение данных - {@link ru.practicum.event.dto.UpdateEventAdminRequest}</li>
 *     <li>Пользовательское изменение данных - {@link ru.practicum.event.dto.UpdateEventUserRequest}</li>
 * </ul>
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
@Builder(toBuilder = true)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 120, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String annotation;

    @Column(length = 7000, nullable = false)
    private String description;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(length = 9, nullable = false)
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column
    private Long views;

    @JoinColumn(name = "initiator_id")
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    private User initiator;

    @JoinColumn(name = "location_id")
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Location.class)
    private Location location;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Category.class)
    private Category category;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, targetEntity = ParticipationRequest.class)
    @JoinColumn(name = "event_id")
    private List<ParticipationRequest> requests;

    @ToString.Exclude
    @ManyToMany(targetEntity = Compilation.class)
    @JoinTable(name = "event_compilation",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "compilation_id"))
    private List<Compilation> compilations = new ArrayList<>();
}
