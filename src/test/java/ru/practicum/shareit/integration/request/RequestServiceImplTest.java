package ru.practicum.shareit.integration.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RequestServiceImplTest {
    private final UserService userService;
    private final ItemRequestService itemRequestService;

    @Order(1)
    @Test
    void testCreateItemRequest() {
        UserDto ownerDto = UserDto.builder().email("user1@mail.ru").name("User1").build();
        userService.createUser(ownerDto);
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("ItemRequest1").build();
        ItemRequestDto itemRequestDto1 = itemRequestService.createItemRequest(itemRequestDto, 1L);
        assertThat(itemRequestDto1.getId(), notNullValue());
        assertThat(itemRequestDto.getDescription(), equalTo(itemRequestDto1.getDescription()));
    }

    @Order(2)
    @Test
    void testGetItemRequest() {
        ItemRequestDto itemRequestDto1 = itemRequestService.getItemRequest(1L, 1L);
        assertThat(itemRequestDto1.getId(), notNullValue());
        assertThat(itemRequestDto1.getDescription(), equalTo("ItemRequest1"));

    }

    @Order(3)
    @Test
    void testGetAllItemRequest() {
        List<ItemRequestDto> itemRequestDtos = itemRequestService.getAllItemRequest(1L);
        assertThat(itemRequestDtos.size(), equalTo(1));
    }

    @Order(4)
    @Test
    void testGetAllItemRequestPaginationWithCorrectFromSize() {
        UserDto ownerDto = UserDto.builder().email("user2@mail.ru").name("User2").build();
        userService.createUser(ownerDto);
        List<ItemRequestDto> itemRequestDtos = itemRequestService.getAllItemRequestWithPagination(2L, 0, 1);
        assertThat(itemRequestDtos.size(), equalTo(1));
    }

    @Order(5)
    @Test
    void testGetAllItemRequestPaginationWithZeroSize() {
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> itemRequestService.getAllItemRequestWithPagination(2L, 0, 0));

        Assertions.assertEquals("Incorrect size or from", exception.getMessage());
    }

    @Order(6)
    @Test
    void testGetAllItemRequestPaginationWithNegativeFrom() {
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> itemRequestService.getAllItemRequestWithPagination(2L, -1, 0));

        Assertions.assertEquals("Incorrect size or from", exception.getMessage());
    }

    @Order(7)
    @Test
    void testCreateItemRequestWithWrongDescription() {

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("").build();
        final ConstraintViolationException exception = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> itemRequestService.createItemRequest(itemRequestDto, 1L));

        Assertions.assertEquals("check.t.description: must not be empty", exception.getMessage());
    }

    @Order(8)
    @Test
    void testCreateItemRequestWithWrongUser() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("fdfdsfsd").build();
        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> itemRequestService.createItemRequest(itemRequestDto, 100L));

        Assertions.assertEquals("User with id=100 not found", exception.getMessage());
    }
}
