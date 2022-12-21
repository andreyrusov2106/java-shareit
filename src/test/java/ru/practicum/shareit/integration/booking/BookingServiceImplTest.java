package ru.practicum.shareit.integration.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test1",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class BookingServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @Test
    void test1CreateBooking() {
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
        BookingDto bookingDtonew = bookingService.createBooking(bookingDto, 2L);
        assertThat(bookingDtonew.getId(), notNullValue());
        assertThat(bookingDtonew.getStart(), equalTo(bookingDto.getStart()));
        assertThat(bookingDtonew.getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(bookingDtonew.getBooker(), equalTo(bookingDto.getBooker()));
        assertThat(bookingDtonew.getItem(), equalTo(bookingDto.getItem()));
    }

    @Test
    void test212CreateBookingFail1() {
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
                () ->  bookingService.createBooking(bookingDto, 100L));

        Assertions.assertEquals("User with id=100 not found", exception.getMessage());
    }

    @Test
    void test2GetBooking() {
        BookingDto bookingDto = bookingService.getBooking(1L, 1L);
        assertThat(bookingDto.getItem().getId(), equalTo(1L));
        assertThat(bookingDto.getBooker().getId(), equalTo(2L));

    }


    @Test
    void test21GetBookingFail2() {
        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () ->  bookingService.getBooking(2L, 1L));

        Assertions.assertEquals("Booking with id not found2", exception.getMessage());
    }

    @Test
    void test3UpdateBooking() {
        BookingDto bookingDto = bookingService.updateBooking(1L, 1L, true);
        assertThat(bookingDto.getStatus(), equalTo(Status.APPROVED));
    }

    @Test
    void test3UpdateBookingFail() {
        final NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () ->  bookingService.updateBooking(2L, 1L, true));

        Assertions.assertEquals("No value present", exception.getMessage());
    }

    @Test
    void test4getAllBookingsByState() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByState(2L, State.ALL.name(), 0, 1);
        assertThat(bookingDtos.size(), equalTo(1));
    }

    @Test
    void test5getPastBookingsByStatePast() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByState(2L, State.PAST.name(), 0, 1);
        assertThat(bookingDtos.size(), equalTo(0));
    }

    @Test
    void test6getPastBookingsByStateCurrent() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByState(2L, State.CURRENT.name(), 0, 1);
        assertThat(bookingDtos.size(), equalTo(0));
    }

    @Test
    void test7getPastBookingsByStateRejected() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByState(2L, State.REJECTED.name(), 0, 1);
        assertThat(bookingDtos.size(), equalTo(0));
    }

    @Test
    void test8getPastBookingsByStateWaiting() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByState(2L, State.WAITING.name(), 0, 1);
        assertThat(bookingDtos.size(), equalTo(0));
    }

    @Test
    void test8getPastBookingsByStateFuture() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByState(2L, State.FUTURE.name(), 0, 1);
        assertThat(bookingDtos.size(), equalTo(1));
    }

    @Test
    void test9getAllBookingsByStateAndOwner() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByStateAndOwner(1L, State.ALL.name(), 0, 1);
        assertThat(bookingDtos.size(), equalTo(1));
    }

    @Test
    void test91getPastBookingsByStateAndOwnerPast() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByStateAndOwner(1L, State.PAST.name(), 0, 1);
        assertThat(bookingDtos.size(), equalTo(0));
    }

    @Test
    void test92getPastBookingsByStateAndOwnerCurrent() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByStateAndOwner(1L, State.CURRENT.name(), 0, 1);
        assertThat(bookingDtos.size(), equalTo(0));
    }

    @Test
    void test93getPastBookingsByStateAndOwnerWaiting() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByStateAndOwner(1L, State.WAITING.name(), 0, 1);
        assertThat(bookingDtos.size(), equalTo(0));
    }

    @Test
    void test94getPastBookingsByStateAndOwnerRejected() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByStateAndOwner(1L, State.REJECTED.name(), 0, 1);
        assertThat(bookingDtos.size(), equalTo(0));
    }

    @Test
    void test95getPastBookingsByStateAndOwnerFuture() {
        List<BookingDto> bookingDtos = bookingService.getAllBookingsByStateAndOwner(1L, State.FUTURE.name(), 0, 1);
        assertThat(bookingDtos.size(), equalTo(1));
    }
}
