package ru.practicum.shareit.item.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.validator.Validator;

import javax.validation.Valid;

@Validated
@Component
public class CommentValidator implements Validator<CommentDto> {
    public void check(@Valid CommentDto commentDto) {
    }
}
