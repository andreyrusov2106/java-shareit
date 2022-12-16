package ru.practicum.shareit.item.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validator.Validator;

import javax.validation.Valid;

@Validated
@Component
public class ItemValidator implements Validator<ItemDto> {
    public void check(@Valid ItemDto itemDto) {
    }
}
