package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .bookerId(booking.getBooker().getId())
                .build();

    }

    public static void toBooking(Booking booking, BookingDto bookingDto) {
        if (bookingDto.getStart() != null) booking.setStart(bookingDto.getStart());
        if (bookingDto.getEnd() != null) booking.setEnd(bookingDto.getEnd());
        if (bookingDto.getStatus() != null) booking.setStatus(bookingDto.getStatus());
    }
}
