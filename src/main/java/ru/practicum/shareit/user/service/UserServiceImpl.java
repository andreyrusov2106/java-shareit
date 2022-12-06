package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validator.Validator;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional()
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final Validator<UserDto> userDtoValidator;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = new User();
        UserMapper.toUser(user, userDto);
        userDtoValidator.check(userDto);
        User createdUser = repository.save(user);
        log.info("User created" + createdUser);
        return UserMapper.toUserDto(createdUser);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User updatedUser;
        var user = repository.findById(id);
        if (user.isPresent()) {
            UserMapper.toUser(user.get(), userDto);
            user.get().setId(id);
            userDtoValidator.check(UserMapper.toUserDto(user.get()));
            updatedUser = repository.save(user.get());
            log.info("User updated" + updatedUser);
            return UserMapper.toUserDto(updatedUser);
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Long id) {
        var user = repository.findById(id);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        return UserMapper.toUserDto(user.get());
    }

    @Override
    public void removeUser(Long id) {
        repository.deleteAllById(Collections.singleton(id));
    }
}
