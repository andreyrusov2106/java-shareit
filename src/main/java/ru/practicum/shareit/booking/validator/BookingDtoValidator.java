package ru.practicum.shareit.booking.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.dto.BookingDto;

import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.validator.Validator;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Validated
@Component
public class BookingDtoValidator implements Validator<BookingDto> {
    public void check(@Valid BookingDto bookingDto) {
        if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("End date is before now");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BadRequestException("End date is before start");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Start date is before now");
        }
    }
}
