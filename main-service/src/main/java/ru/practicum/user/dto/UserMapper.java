package ru.practicum.user.dto;

import ru.practicum.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static List<UserDto> mapToUserDto(final Iterable<User> users) {
        List<UserDto> dtos = new ArrayList<>();

        for (User user : users) {
            dtos.add(mapToUserDto(user));
        }

        return dtos;
    }

    public static UserShortDto mapToUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static List<UserShortDto> mapToUserShortDto(final Iterable<User> users) {
        List<UserShortDto> dtos = new ArrayList<>();

        for (User user : users) {
            dtos.add(mapToUserShortDto(user));
        }

        return dtos;
    }

    public static User mapToUser(NewUserRequest userRequestDto) {
        return User.builder()
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .build();
    }
}
