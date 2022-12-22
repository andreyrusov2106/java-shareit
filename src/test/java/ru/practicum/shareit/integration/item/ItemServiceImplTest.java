package ru.practicum.shareit.integration.item;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
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


@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test1",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ItemServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @Test
    void test1SaveItem() {
        UserDto userDto = UserDto.builder().id(1L).email("user1@mail.ru").name("User1").build();
        userService.createUser(userDto);
        ItemDto itemDto = ItemDto.builder().description("Item1desc").name("Item1").available(true).build();
        ItemDto item = itemService.createItem(itemDto, 1L);
        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(itemDto.getName()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(item.getAvailable(), equalTo(itemDto.getAvailable()));
    }

    @Test
    void test2UpdateItem() {
        ItemDto itemDto = ItemDto.builder().description("Item1descNew").name("Item1New").available(true).build();
        ItemDto item = itemService.updateItem(itemDto, 1L, 1L);
        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(itemDto.getName()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(item.getAvailable(), equalTo(itemDto.getAvailable()));
    }

    @Test
    void test3GetItem() {
        ItemDto itemDto = ItemDto.builder().description("Item1descNew").name("Item1New").available(true).build();
        ItemDto item = itemService.getItem(1L, 1L);
        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(itemDto.getName()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(item.getAvailable(), equalTo(itemDto.getAvailable()));
    }

    @Test
    void test4Search() {
        List<ItemDto> items = itemService.search("Item1");
        assertThat(items.size(), equalTo(1));
    }

    @Test
    void test5CreateComment() throws InterruptedException {
        UserDto userDto = UserDto.builder().email("user2@mail.ru").name("User2").build();
        UserDto user1 = userService.createUser(userDto);
        User user = new User();
        UserMapper.toUser(user, user1);
        LocalDateTime start = LocalDateTime.now().plusSeconds(1);
        LocalDateTime end = LocalDateTime.now().plusSeconds(2);
        CommentDto commentDto = CommentDto.builder().text("Comment1").created(LocalDateTime.now()).authorName("User1").build();
        BookingDto bookingDto = BookingDto.builder().itemId(1L).booker(user)
                .start(start)
                .end(end)
                .build();
        bookingService.createBooking(bookingDto, 2L);
        Thread.sleep(2000);
        itemService.createComment(commentDto, 2L, 1L);
        ItemDto item = itemService.getItem(1L, 1L);
        assertThat(item.getComments().size(), equalTo(1));
    }

    @Test
    void test6DeleteItem() {
        ItemDto itemDto = ItemDto.builder().description("Item2desc").name("Item2").available(true).build();
        itemService.createItem(itemDto, 2L);
        itemService.removeItem(2L);
        List<ItemDto> items = itemService.getAllItemsByUserId(2L);
        assertThat(items.size(), equalTo(0));
    }

    @Test
    void test7GetWrongItem() {
        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> itemService.getItem(99L, 1L));
        Assertions.assertEquals("Item with id not found99", exception.getMessage());
    }

}
