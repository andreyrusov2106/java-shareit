package ru.practicum.shareit.booking.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping()
    public BookingDto createItem(@RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateItem(@PathVariable Long bookingId,
                                 @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestParam(name = "approved") Boolean approved) {
        return bookingService.updateBooking(bookingId, userId, approved);

    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long bookingId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping()
    public List<BookingDto> getAllBookingsByState(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(name = "state", required = false) String state,
                                                  @RequestParam(name = "from", required = false) Integer from,
                                                  @RequestParam(name = "size", required = false) Integer size) {
        return bookingService.getAllBookingsByState(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsByStateAndOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @RequestParam(name = "state", required = false) String state,
                                                          @RequestParam(name = "from", required = false) Integer from,
                                                          @RequestParam(name = "size", required = false) Integer size) {
        return bookingService.getAllBookingsByStateAndOwner(userId, state, from, size);
    }
}
