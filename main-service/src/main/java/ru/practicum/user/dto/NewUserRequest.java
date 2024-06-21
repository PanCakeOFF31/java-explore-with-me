package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Класс, используемый для создания сущности-пользователь - {@link ru.practicum.user.model.User}
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class NewUserRequest {

    @NotBlank(message = "Имя пользователя User.name не может быть null/empty/blank")
    @Size(min = 2, max = 250, message = "User.name - Минимальная длина имени - {min}, а максимальная {max} символов")
    private String name;

    @NotBlank(message = "Почта пользователя User.email не может быть null/empty/blank")
    @Email(message = "User.email - почта не соответствует шаблону @Email")
    @Size(min = 6, max = 254, message = "User.email - Минимальная длина почты - {min}, а максимальная {max} символов")
    private String email;
}
