package ru.practicum.shareit.booking.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.validator.Validator;

import javax.validation.Valid;

@Validated
@Component
public class BookingDtoValidator implements Validator<BookItemRequestDto> {
    public void check(@Valid BookItemRequestDto bookingDto) {
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BadRequestException("End date is before start");
        }
    }
}
