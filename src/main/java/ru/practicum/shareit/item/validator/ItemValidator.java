package ru.practicum.shareit.item.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.validator.Validator;

import javax.validation.Valid;

@Validated
@Component
public class ItemValidator implements Validator<Item> {
    public void check(@Valid Item item) {
    }
}
