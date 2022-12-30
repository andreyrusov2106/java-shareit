package ru.practicum.shareit.unit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.validator.BookingDtoValidator;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.IllegalEnumStateException;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    BookingRepository bookingRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookingDtoValidator bookingDtoValidator;
    BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(
                itemRepository,
                userRepository,
                bookingRepository,
                bookingDtoValidator);
    }

    @Test
    public void testCreateBooking() {
        LocalDateTime start = LocalDateTime.now().plusSeconds(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        ItemDto itemDto = ItemDto.builder().id(1L).description("Item5desc").name("Item5").available(true).build();
        Item item1 = new Item();
        ItemMapper.toItem(item1, itemDto);
        BookingDto bookingDto = BookingDto.builder().itemId(1L).booker(null)
                .start(start)
                .end(end)
                .item(item1)
                .build();
        User u = new User(1L, "User1", "iuser1@mail.ru");
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(u));
        Mockito
                .when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.createBooking(bookingDto, 100L));

        Assertions.assertEquals("Item with id=1 not found", exception.getMessage());

    }

    @Test
    public void testUpdateBookingWrongOwner() {
        LocalDateTime start = LocalDateTime.now().plusSeconds(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        UserDto bookerDto = UserDto.builder().id(3L).email("user4@mail.ru").name("User4").build();
        User userBooker = new User();
        UserMapper.toUser(userBooker, bookerDto);
        User u = new User(1L, "User1", "iuser1@mail.ru");
        ItemDto itemDto = ItemDto.builder().description("Item5desc").name("Item5").available(true).build();
        Item item1 = new Item();
        ItemMapper.toItem(item1, itemDto);
        item1.setOwner(u);

        BookingDto bookingDto = BookingDto.builder().id(1L).itemId(1L).booker(userBooker)
                .start(start)
                .end(end)
                .item(item1)
                .build();
        Booking b = new Booking();
        b.setItem(item1);
        BookingMapper.toBooking(b, bookingDto);

        Mockito
                .when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(b));

        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.updateBooking(100L, 100L, true));

        Assertions.assertEquals("User is not owner of item!", exception.getMessage());

    }

    @Test
    public void testUpdateBookingWrongStatus() {
        LocalDateTime start = LocalDateTime.now().plusSeconds(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        UserDto bookerDto = UserDto.builder().id(3L).email("user4@mail.ru").name("User4").build();
        User userBooker = new User();
        UserMapper.toUser(userBooker, bookerDto);
        User u = new User(1L, "User1", "iuser1@mail.ru");
        ItemDto itemDto = ItemDto.builder().description("Item5desc").name("Item5").available(true).build();
        Item item1 = new Item();
        ItemMapper.toItem(item1, itemDto);
        item1.setOwner(u);

        BookingDto bookingDto = BookingDto.builder().id(1L).itemId(1L).booker(userBooker)
                .start(start)
                .end(end)
                .item(item1)
                .status(Status.APPROVED)
                .build();
        Booking b = new Booking();
        b.setItem(item1);
        BookingMapper.toBooking(b, bookingDto);

        Mockito
                .when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(b));

        final IllegalEnumStateException exception = Assertions.assertThrows(
                IllegalEnumStateException.class,
                () -> bookingService.updateBooking(100L, 1L, true));

        Assertions.assertEquals("Status is changer already", exception.getMessage());

    }

    @Test
    public void testAllBookingsByState() {
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> bookingService.getAllBookingsByState(1L, "fdsfds", -1, 0));

        Assertions.assertEquals("Incorrect size or from", exception.getMessage());
    }

    @Test
    public void testAllBookingsByStateWrongUser() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.getAllBookingsByState(1L, "fdsfds", null, null));

        Assertions.assertEquals("User with id=1 not found", exception.getMessage());
    }

    @Test
    public void getAllBookingsByStateAndOwnerWrongUser() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.getAllBookingsByStateAndOwner(1L, "fdsfds", null, null));

        Assertions.assertEquals("User with id=1 not found", exception.getMessage());
    }
}
