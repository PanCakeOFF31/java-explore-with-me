package ru.practicum.user.service;

import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.exception.UserFieldOccupiedException;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto createUser(NewUserRequest newUserRequest) throws UserFieldOccupiedException;

    void deleteUser(long userId);
}
