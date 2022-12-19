package ru.practicum.shareit.request.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.validator.Validator;

import javax.validation.Valid;

@Validated
@Component
public class ItemRequestDtoValidator implements Validator<ItemRequestDto> {
    public void check(@Valid ItemRequestDto itemRequestDto) {
    }
}

