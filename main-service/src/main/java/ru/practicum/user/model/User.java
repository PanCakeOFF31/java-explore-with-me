package ru.practicum.user.model;

import lombok.*;

import javax.persistence.*;

/**
 * Dto-классы - {@link ru.practicum.user.dto}
 * <ul>
 *     <li>Для создания класса - {@link ru.practicum.user.dto.NewUserRequest}</li>
 *     <li >Краткая форма - {@link ru.practicum.user.dto.UserShortDto}</li>
 *     <li >Полная форма - {@link ru.practicum.user.dto.UserDto}</li>
 * </ul>
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "person")
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 250, nullable = false)
    private String name;

    @Column(length = 254, nullable = false, unique = true)
    private String email;
}
