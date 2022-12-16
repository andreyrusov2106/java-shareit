package ru.practicum.shareit.user.validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validator.Validator;

import javax.validation.Valid;

@Validated
@Service
public class UserValidator implements Validator<UserDto> {
    @Override
    public void check(@Valid UserDto userdto) {

    }
}
