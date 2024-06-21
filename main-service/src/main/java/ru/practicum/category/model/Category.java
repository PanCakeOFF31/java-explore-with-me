package ru.practicum.category.model;

import lombok.*;

import javax.persistence.*;

/**
 * Dto-классы - {@link ru.practicum.category.dto}
 * <ul>
 *  <li>Создание класса - {@link ru.practicum.category.dto.NewCategoryDto}</li>
 *  <li>Представление данных - {@link ru.practicum.category.dto.CategoryDto}</li>
 * </ul>
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
@Builder(toBuilder = true)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;
}
