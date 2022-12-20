package ru.practicum.shareit.integration.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;


import javax.validation.ConstraintViolationException;
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
public class RequestServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemRequestService itemRequestService;

    @Test
    void test1CreateItemRequest() {
        UserDto ownerDto = UserDto.builder().email("user1@mail.ru").name("User1").build();
        UserDto owner = userService.createUser(ownerDto);
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("ItemRequest1").build();
        ItemRequestDto itemRequestDto1 = itemRequestService.createItemRequest(itemRequestDto, 1L);
        assertThat(itemRequestDto1.getId(), notNullValue());
        assertThat(itemRequestDto.getDescription(), equalTo(itemRequestDto1.getDescription()));
    }


    @Test
    void test2GetItemRequest() {
        ItemRequestDto itemRequestDto1 = itemRequestService.getItemRequest(1L, 1L);
        assertThat(itemRequestDto1.getId(), notNullValue());
        assertThat(itemRequestDto1.getDescription(), equalTo("ItemRequest1"));

    }

    @Test
    void test3GetAllItemRequest() {
        List<ItemRequestDto> itemRequestDtos = itemRequestService.getAllItemRequest(1L);
        assertThat(itemRequestDtos.size(), equalTo(1));
    }

    @Test
    void test4GetAllItemRequestPagination() {
        UserDto ownerDto = UserDto.builder().email("user2@mail.ru").name("User2").build();
        UserDto owner = userService.createUser(ownerDto);
        List<ItemRequestDto> itemRequestDtos = itemRequestService.getAllItemRequestWithPagination(2L, 0, 1);
        assertThat(itemRequestDtos.size(), equalTo(1));
    }

    @Test
    void test5GetAllItemRequestPagination() {
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> itemRequestService.getAllItemRequestWithPagination(2L, 0, 0));

        Assertions.assertEquals("Incorrect size or from", exception.getMessage());
    }

    @Test
    void test6GetAllItemRequestPagination() {
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> itemRequestService.getAllItemRequestWithPagination(2L, -1, 0));

        Assertions.assertEquals("Incorrect size or from", exception.getMessage());
    }

    @Test
    void test6CreateItemRequestWithWrongDescription() {

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("").build();
        final ConstraintViolationException exception = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> itemRequestService.createItemRequest(itemRequestDto, 1L));

        Assertions.assertEquals("check.t.description: must not be empty", exception.getMessage());
    }

    @Test
    void test7CreateItemRequestWithWrongUser() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("fdfdsfsd").build();
        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> itemRequestService.createItemRequest(itemRequestDto, 100L));

        Assertions.assertEquals("User with id=100 not found", exception.getMessage());
    }
}
