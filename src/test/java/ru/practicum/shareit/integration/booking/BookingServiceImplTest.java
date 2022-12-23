package ru.practicum.shareit.integration.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test1",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @Order(1)
    @Test
    void testCreateBooking() {
        UserDto ownerDto = UserDto.builder().email("user1@mail.ru").name("User1").build();
        UserDto owner = userService.createUser(ownerDto);
        User userOwner = new User();
        UserMapper.toUser(userOwner, owner);
        UserDto bookerDto = UserDto.builder().email("user2@mail.ru").name("User2").build();
        UserDto booker = userService.createUser(bookerDto);
        User userBooker = new User();
        UserMapper.toUser(userBooker, booker);
        ItemDto itemDto = ItemDto.builder().description("Item1desc").name("Item1").available(true).build();
        ItemDto item = itemService.createItem(itemDto, 1L);
        Item item1 = new Item();
        ItemMapper.toItem(item1, item);
        item1.setOwner(userOwner);
        LocalDateTime start = LocalDateTime.now().plusSeconds(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        BookingDto bookingDto = BookingDto.builder().itemId(1L).booker(userBooker)
                .start(start)
                .end(end)
                .item(item1)
                .build();
        BookingDto bookingDtoNew = bookingService.createBooking(bookingDto, 2L);
        assertThat(bookingDtoNew.getId(), notNullValue());
        assertThat(bookingDtoNew.getStart(), equalTo(bookingDto.getStart()));
        assertThat(bookingDtoNew.getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(bookingDtoNew.getBooker(), equalTo(bookingDto.getBooker()));
        assertThat(bookingDtoNew.getItem(), equalTo(bookingDto.getItem()));
    }

    @Order(2)
    @Test
    void testCreateBookingFailUser() {
        UserDto ownerDto = UserDto.builder().email("user3@mail.ru").name("User3").build();
        UserDto owner = userService.createUser(ownerDto);
        User userOwner = new User();
        UserMapper.toUser(userOwner, owner);
        UserDto bookerDto = UserDto.builder().email("user4@mail.ru").name("User4").build();
        UserDto booker = userService.createUser(bookerDto);
        booker.setId(3L);
        User userBooker = new User();
        UserMapper.toUser(userBooker, booker);
        ItemDto itemDto = ItemDto.builder().description("Item5desc").name("Item5").available(true).build();
        ItemDto item = itemService.createItem(itemDto, 1L);
        Item item1 = new Item();
        ItemMapper.toItem(item1, item);
        item1.setOwner(userOwner);
        LocalDateTime start = LocalDateTime.now().plusSeconds(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        BookingDto bookingDto = BookingDto.builder().itemId(1L).booker(userBooker)
                .start(start)
                .end(end)
                .item(item1)
                .build();
        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.createBooking(bookingDto, 100L));

        Assertions.assertEquals("User with id=100 not found", exception.getMessage());
    }

    @Order(3)
    @Test
    void testGetBooking() {
        BookingDto bookingDto = bookingService.getBooking(1L, 1L);
        assertThat(bookingDto.getItem().getId(), equalTo(1L));
        assertThat(bookingDto.getBooker().getId(), equalTo(2L));

    }

    @Order(4)
    @Test
    void testGetBookingFailBooking() {
        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.getBooking(2L, 1L));

        Assertions.assertEquals("Booking with id not found2", exception.getMessage());
    }

    @Order(5)
    @Test
    void testUpdateBooking() {
        BookingDto bookingDto = bookingService.updateBooking(1L, 1L, true);
        assertThat(bookingDto.getStatus(), equalTo(Status.APPROVED));
    }

    @Order(6)
    @Test
    void testUpdateBookingFail() {
        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.updateBooking(2L, 1L, true));

        Assertions.assertEquals("Booking not found", exception.getMessage());
    }

    @Order(7)
    @Test
    void testgetAllBookingsByState() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByState(2L, State.ALL.name(), 0, 1);
        assertThat(bookingDtos.size(), equalTo(1));
    }

    @Order(8)
    @Test
    void testgetPastBookingsByStatePast() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByState(2L, State.PAST.name(), 0, 1);
        assertTrue(bookingDtos.isEmpty());
    }

    @Order(9)
    @Test
    void testgetPastBookingsByStateCurrent() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByState(2L, State.CURRENT.name(), 0, 1);
        assertTrue(bookingDtos.isEmpty());
    }

    @Order(10)
    @Test
    void testgetPastBookingsByStateRejected() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByState(2L, State.REJECTED.name(), 0, 1);
        assertTrue(bookingDtos.isEmpty());
    }

    @Order(11)
    @Test
    void testGetPastBookingsByStateWaiting() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByState(2L, State.WAITING.name(), 0, 1);
        assertTrue(bookingDtos.isEmpty());
    }

    @Order(12)
    @Test
    void testGetPastBookingsByStateFuture() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByState(2L, State.FUTURE.name(), 0, 1);
        assertThat(bookingDtos.size(), equalTo(1));
    }

    @Order(13)
    @Test
    void testGetAllBookingsByStateAndOwner() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByStateAndOwner(1L, State.ALL.name(), 0, 1);
        assertThat(bookingDtos.size(), equalTo(1));
    }

    @Order(14)
    @Test
    void testGetPastBookingsByStateAndOwnerPast() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByStateAndOwner(1L, State.PAST.name(), 0, 1);
        assertTrue(bookingDtos.isEmpty());
    }

    @Order(15)
    @Test
    void testGetPastBookingsByStateAndOwnerCurrent() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByStateAndOwner(1L, State.CURRENT.name(), 0, 1);
        assertTrue(bookingDtos.isEmpty());
    }

    @Order(16)
    @Test
    void testGetPastBookingsByStateAndOwnerWaiting() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByStateAndOwner(1L, State.WAITING.name(), 0, 1);
        assertTrue(bookingDtos.isEmpty());
    }

    @Order(17)
    @Test
    void testGetPastBookingsByStateAndOwnerRejected() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByStateAndOwner(1L, State.REJECTED.name(), 0, 1);
        assertTrue(bookingDtos.isEmpty());
    }

    @Order(18)
    @Test
    void testGetPastBookingsByStateAndOwnerFuture() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByStateAndOwner(1L, State.FUTURE.name(), 0, 1);
        assertThat(bookingDtos.size(), equalTo(1));
    }
}
