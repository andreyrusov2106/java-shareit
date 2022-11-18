package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ResourceAlreadyExistsException;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validator.Validator;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final Validator<User> userValidator;

    @Autowired
    public UserService(UserRepository userRepository, Validator<User> userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    public UserDto createUser(UserDto userDto) {
        User user = new User();
        UserMapper.toUser(user, userDto);
        userValidator.check(user);
        if (userRepository.checkEmail(user)) {
            throw new ResourceAlreadyExistsException("Resource already exists");
        }
        User createdUser = userRepository.createUser(user);
        log.info("User created" + createdUser);
        return UserMapper.toUserDto(createdUser);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User updatedUser;
        if (userRepository.contains(id)) {
            User user = new User(userRepository.getUser(id));
            UserMapper.toUser(user, userDto);
            user.setId(id);
            if (userRepository.checkEmail(user)) {
                throw new ResourceAlreadyExistsException("Resource already exists");
            }
            updatedUser = userRepository.updateUser(user);
            userValidator.check(updatedUser);
            log.info("User updated" + updatedUser);
        } else {
            throw new ResourceNotFoundException("User not found" + id);
        }
        return UserMapper.toUserDto(updatedUser);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUser(Long id) {
        if (userRepository.contains(id)) {
            return UserMapper.toUserDto(userRepository.getUser(id));
        } else {
            throw new ResourceNotFoundException("User with id not found" + id);
        }
    }

    public void removeUser(Long id) {
        if (!userRepository.removeUser(id)) {
            throw new ResourceNotFoundException("User not found");
        }
    }
}
