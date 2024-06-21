package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.component.CommonComponent;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.exception.UserFieldOccupiedException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final CommonComponent commonComponent;
    private final UserRepository userRepository;

    private static final String USER_EMAIL_OCCUPIED = "Потенциальное нарушение целостности данных. Поле User.email: '%s' уже занято.";

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        log.debug("UserServiceImpl - service.getUsers({}, {}, {})", ids, from, size);

        if (ids == null) {
            Pageable pageable = commonComponent.definePageable(from, size);
            return UserMapper.mapToUserDto(userRepository.findAll(pageable));
        }

        return UserMapper.mapToUserDto(userRepository.findAllByIdIn(ids));
    }

    @Override
    @Transactional
    public UserDto createUser(NewUserRequest newUserRequest) throws UserFieldOccupiedException {
        log.debug("UserServiceImpl - service.createUser({})", newUserRequest);

        toCreateFieldValidation(newUserRequest.getEmail());

        User userToCreate = UserMapper.mapToUser(newUserRequest);
        return UserMapper.mapToUserDto(userRepository.save(userToCreate));
    }

    private void toCreateFieldValidation(String email) {
        log.debug("UserServiceImpl - service.toCreateFieldValidation({})", email);

        Optional<User> foundUser = userRepository.findUserByEmail(email);

        if (foundUser.isPresent())
            commonComponent.throwAndLog(() -> new UserFieldOccupiedException(commonComponent
                    .prepareMessage(USER_EMAIL_OCCUPIED, email)));

    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        log.debug("UserServiceImpl - service.deleteUser({})", userId);
        commonComponent.userExists(userId);
        userRepository.deleteById(userId);
    }
}
