package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.IllegalEnumStateException;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public BookingDto createBooking(BookingDto bookingDto, Long userId) {
        Booking newBooking = new Booking();
        BookingMapper.toBooking(newBooking, bookingDto);
        var booker = userRepository.findById(userId);
        if (booker.isEmpty()) {
            throw new ResourceNotFoundException(String.format("User with id=%d not found", userId));
        } else {
            newBooking.setBooker(booker.get());
        }
        var item = itemRepository.findById(bookingDto.getItemId());
        if (item.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Item with id=%d not found", bookingDto.getItemId()));
        } else {
            if (item.get().getAvailable()) {
                if (!booker.get().equals(item.get().getOwner())) {
                    newBooking.setItem(item.get());
                } else {
                    throw new ResourceNotFoundException(String.format("Item with id=%d has different owner", item.get().getId()));
                }
            } else {
                throw new BadRequestException(String.format("Item with id=%d not available", item.get().getId()));
            }
        }
        newBooking.setStatus(Status.WAITING);
        Booking createdBooking = bookingRepository.save(newBooking);
        log.info("Booking created" + createdBooking);
        return BookingMapper.toBookingDto(createdBooking);
    }

    @Override
    public BookingDto getBooking(Long id, Long userId) {
        var booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            if (!Objects.equals(booking.get().getBooker().getId(), userId) &&
                    !Objects.equals(booking.get().getItem().getOwner().getId(), userId)) {
                throw new ResourceNotFoundException("User is not owner of item!");
            }
            return BookingMapper.toBookingDto(booking.get());
        } else {
            throw new ResourceNotFoundException("Booking with id not found" + id);
        }
    }

    @Override
    public BookingDto updateBooking(Long bookingId, Long userId, Boolean approved) {
        Booking updatedBooking;
        Booking booking = new Booking(bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found")));
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new ResourceNotFoundException("User is not owner of item!");
        }
        if ((booking.getStatus().equals(Status.APPROVED) && approved)
                || (booking.getStatus().equals(Status.REJECTED) && !approved)) {
            throw new IllegalEnumStateException("Status is changer already");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        updatedBooking = bookingRepository.save(booking);
        log.info("Booking updated" + updatedBooking);
        return BookingMapper.toBookingDto(updatedBooking);
    }

    @Override
    public List<BookingDto> getAllBookingsByState(Long userId,
                                                  String stringState,
                                                  Integer from,
                                                  Integer size) {
        Pageable pageable = sizeAndFromToPageable(from, size);
        var booker = userRepository.findById(userId);
        if (booker.isEmpty()) {
            throw new ResourceNotFoundException(String.format("User with id=%d not found", userId));
        }
        State state = State.stringToState(stringState);
        return stateToRepository(booker.get(), state, pageable)
                .stream()
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .collect(Collectors.toList());
    }

    private Pageable sizeAndFromToPageable(Integer from,
                                           Integer size) {
        return PageRequest.of(from / size, size, Sort.by("start").descending());
    }

    @Override
    public List<BookingDto> getAllBookingsByStateAndOwner(Long userId,
                                                          String stringState,
                                                          Integer from,
                                                          Integer size) {
        Pageable pageable = sizeAndFromToPageable(from, size);
        var booker = userRepository.findById(userId);
        if (booker.isEmpty()) {
            throw new ResourceNotFoundException(String.format("User with id=%d not found", userId));
        }
        State state = State.stringToState(stringState);
        return stateToRepositoryAndOwner(booker.get(), state, pageable)
                .stream()
                .filter(b -> Objects.equals(b.getItem().getOwner().getId(), userId))
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .collect(Collectors.toList());
    }

    private List<BookingDto> stateToRepositoryAndOwner(User owner, State state, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> resultList = new ArrayList<>();
        Page<Booking> result = new PageImpl<>(resultList);
        switch (state) {
            case ALL:
                result = bookingRepository.findAllByOwnerOfItem(owner.getId(), pageable);
                break;
            case CURRENT:
                result = bookingRepository.findAllByOwnerOfItemAndStartIsBeforeAndEndIsAfter(owner, now, pageable);
                break;
            case PAST:
                result = bookingRepository.findAllByOwnerOfItemAndEndIsBeforeAndStatusIs(owner.getId(), now, Status.APPROVED, pageable);
                break;
            case FUTURE:
                result = bookingRepository.findAllByOwnerOfItemAndStartIsAfter(owner.getId(), now, pageable);
                break;
            case WAITING:
                result = bookingRepository.findAllByOwnerOfItemAndStatusIs(owner.getId(), Status.WAITING, pageable);
                break;
            case REJECTED:
                result = bookingRepository.findAllByOwnerOfItemAndStatusIs(owner.getId(), Status.REJECTED, pageable);
                break;
        }
        return result.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private List<BookingDto> stateToRepository(User owner, State state, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> resultList = new ArrayList<>();
        Page<Booking> result = new PageImpl<>(resultList);
        switch (state) {
            case ALL:
                result = bookingRepository.findBookingsByBooker(owner, pageable);
                break;
            case CURRENT:
                result = bookingRepository.findAllByBookerAndStartIsBeforeAndEndIsAfter(owner, now, now, pageable);
                break;
            case PAST:
                result = bookingRepository.findAllByBookerAndEndIsBeforeAndStatusIs(owner, now, Status.APPROVED, pageable);
                break;
            case FUTURE:
                result = bookingRepository.findAllByBookerAndStartIsAfter(owner, now, pageable);
                break;
            case WAITING:
                result = bookingRepository.findAllByBookerAndStatusIs(owner, Status.WAITING, pageable);
                break;
            case REJECTED:
                result = bookingRepository.findAllByBookerAndStatusIs(owner, Status.REJECTED, pageable);
                break;
        }
        return result.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }


}
