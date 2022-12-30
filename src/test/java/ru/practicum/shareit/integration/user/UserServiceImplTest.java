package ru.practicum.shareit.integration.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ConstraintViolationException;
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
public class UserServiceImplTest {
    private final UserService service;

    @Order(1)
    @Test
    void testSaveUser() {
        UserDto userDto = UserDto.builder().id(1L).email("user1@mail.ru").name("User1").build();
        UserDto user = service.createUser(userDto);
        assertThat(user.getId(), notNullValue());
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
        assertThat(user.getName(), equalTo(userDto.getName()));
    }

    @Order(2)
    @Test
    void testUpdateUser() {
        UserDto userDto2 = UserDto.builder().email("user1@mail.ru").name("User1New").build();
        UserDto user = service.updateUser(1L, userDto2);
        assertThat(user.getId(), notNullValue());
        assertThat(user.getEmail(), equalTo(userDto2.getEmail()));
        assertThat(user.getName(), equalTo(userDto2.getName()));
    }

    @Order(3)
    @Test
    void testGetAllUsers() {
        List<UserDto> users = service.getAllUsers();
        assertThat(users.size(), equalTo(1));

    }

    @Order(4)
    @Test
    void testGetUser() {
        UserDto userDto2 = UserDto.builder().email("user1@mail.ru").name("User1New").build();
        UserDto user = service.getUser(1L);
        assertThat(user.getId(), notNullValue());
        assertThat(user.getEmail(), equalTo(userDto2.getEmail()));
        assertThat(user.getName(), equalTo(userDto2.getName()));
    }

    @Order(5)
    @Test
    void testSaveUserWithDuplicateEmail() {
        UserDto userDto = UserDto.builder().email("user1@mail.ru").name("User1").build();
        final DataIntegrityViolationException exception = Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> service.createUser(userDto));

        Assertions.assertEquals("could not execute statement; SQL [n/a]; " +
                "constraint [null]; nested exception " +
                "is org.hibernate.exception.ConstraintViolationException: " +
                "could not execute statement", exception.getMessage());
    }

    @Order(6)
    @Test
    void testRemoveUser() {
        service.removeUser(1L);
        List<UserDto> users = service.getAllUsers();
        assertTrue(users.isEmpty());
    }

    @Order(7)
    @Test
    void testCreateUserWrongEmail() {
        UserDto userDto = UserDto.builder().email("").name("User1").build();
        ItemRequestDto.builder()
                .description("").build();
        final ConstraintViolationException exception = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> service.createUser(userDto));

        Assertions.assertEquals("check.t.email: must not be empty", exception.getMessage());
    }

    @Order(8)
    @Test
    void testUpdateWrongUser() {
        UserDto userDto = UserDto.builder().email("").name("User1").build();
        ItemRequestDto.builder()
                .description("").build();
        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateUser(99L, userDto));

        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Order(9)
    @Test
    void testGetWrongUser() {
        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> service.getUser(99L));

        Assertions.assertEquals("User not found", exception.getMessage());
    }


}
