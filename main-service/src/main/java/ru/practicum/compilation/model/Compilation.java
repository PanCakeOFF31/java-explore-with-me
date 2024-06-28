package ru.practicum.compilation.model;

import lombok.*;
import ru.practicum.event.model.Event;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Dto-классы - {@link ru.practicum.compilation.dto}
 * <ul>
 *  <li>Создание класса - {@link ru.practicum.compilation.dto.NewCompilationDto}</li>
 *  <li>Обновление данных - {@link ru.practicum.compilation.dto.UpdateCompilationRequest}</li>
 *  <li>Представление данных - {@link ru.practicum.compilation.dto.CompilationDto}</li>
 * </ul>
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "compilation")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(nullable = false)
    private Boolean pinned;

    @ToString.Exclude
    @ManyToMany(targetEntity = Event.class)
    @JoinTable(name = "event_compilation",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events = new ArrayList<>();
}
