package ru.practicum.shareit.unit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.validator.BookingDtoValidator;
import ru.practicum.shareit.exceptions.BadRequestException;

import java.time.LocalDateTime;

public class BookingValidatorTest {

    private static BookingDtoValidator bookingDtoValidator;

    @BeforeAll
    public static void beforeAll() {
        bookingDtoValidator = new BookingDtoValidator();
    }

    @Test
    public void testBookingValidatorEndBeforeNow() {
        LocalDateTime start = LocalDateTime.now().plusSeconds(1);
        LocalDateTime end = LocalDateTime.now().minusDays(2);
        BookingDto bookingDto = BookingDto.builder().itemId(1L).booker(null)
                .start(start)
                .end(end)
                .item(null)
                .build();

        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> bookingDtoValidator.check(bookingDto));

        Assertions.assertEquals("End date is before now", exception.getMessage());

    }

    @Test
    public void testBookingValidatorEndBeforeStart() {
        LocalDateTime start = LocalDateTime.now().plusSeconds(100);
        LocalDateTime end = LocalDateTime.now().plusSeconds(50);
        BookingDto bookingDto = BookingDto.builder().itemId(1L).booker(null)
                .start(start)
                .end(end)
                .item(null)
                .build();

        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> bookingDtoValidator.check(bookingDto));

        Assertions.assertEquals("End date is before start", exception.getMessage());
    }

    @Test
    public void testBookingValidatorStartBeforeNow() {
        LocalDateTime start = LocalDateTime.now().minusSeconds(100);
        LocalDateTime end = LocalDateTime.now().plusSeconds(50);
        BookingDto bookingDto = BookingDto.builder().itemId(1L).booker(null)
                .start(start)
                .end(end)
                .item(null)
                .build();

        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> bookingDtoValidator.check(bookingDto));

        Assertions.assertEquals("Start date is before now", exception.getMessage());
    }

}
