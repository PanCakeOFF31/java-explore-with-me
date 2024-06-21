package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class AdminUserController {
    private final UserService userService;

    /**
     * <h5>Получение информации о пользователях</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     * <li>возвращает информацию обо всех пользователях (учитываются параметры ограничения выборки), либо о конкретных (учитываются указанные идентификаторы)</li>
     * <li>в случае, если по заданным фильтрам не найдено ни одного пользователя, возвращает пустой список</li>
     * </ul>
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                  @RequestParam(defaultValue = "10") @Positive final int size) {
        log.debug("/admin/users - GET: getUsers({}, {}, {})", ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    /**
     * <h5>Добавление нового пользователя</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li></li>
     * </ul>
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.debug("/admin/users - POST: createUser({})", newUserRequest);
        return userService.createUser(newUserRequest);
    }

    /**
     * <h5>Удаление пользователя</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li></li>
     * </ul>
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long userId) {
        log.debug("/admin/users/{} - DELETED: deleteUser({})", userId, userId);
        userService.deleteUser(userId);
    }
}