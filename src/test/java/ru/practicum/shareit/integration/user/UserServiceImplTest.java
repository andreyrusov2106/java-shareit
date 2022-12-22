package ru.practicum.shareit.integration.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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


@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test1",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class UserServiceImplTest {
    private final UserService service;

    @Test
    void test1SaveUser() {
        UserDto userDto = UserDto.builder().id(1L).email("user1@mail.ru").name("User1").build();
        UserDto user = service.createUser(userDto);
        assertThat(user.getId(), notNullValue());
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
        assertThat(user.getName(), equalTo(userDto.getName()));
    }

    @Test
    void test2UpdateUser() {
        UserDto userDto2 = UserDto.builder().email("user1@mail.ru").name("User1New").build();
        UserDto user = service.updateUser(1L, userDto2);
        assertThat(user.getId(), notNullValue());
        assertThat(user.getEmail(), equalTo(userDto2.getEmail()));
        assertThat(user.getName(), equalTo(userDto2.getName()));
    }

    @Test
    void test3GetAllUsers() {
        List<UserDto> users = service.getAllUsers();
        assertThat(users.size(), equalTo(1));

    }

    @Test
    void test4GetUser() {
        UserDto userDto2 = UserDto.builder().email("user1@mail.ru").name("User1New").build();
        UserDto user = service.getUser(1L);
        assertThat(user.getId(), notNullValue());
        assertThat(user.getEmail(), equalTo(userDto2.getEmail()));
        assertThat(user.getName(), equalTo(userDto2.getName()));
    }

    @Test
    void test5SaveUserWithDuplicateEmail() {
        UserDto userDto = UserDto.builder().email("user1@mail.ru").name("User1").build();
        final DataIntegrityViolationException exception = Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> service.createUser(userDto));

        Assertions.assertEquals("could not execute statement; SQL [n/a]; " +
                "constraint [null]; nested exception " +
                "is org.hibernate.exception.ConstraintViolationException: " +
                "could not execute statement", exception.getMessage());
    }

    @Test
    void test6RemoveUser() {
        service.removeUser(1L);
        List<UserDto> users = service.getAllUsers();
        assertThat(users.size(), equalTo(0));
    }

    @Test
    void test6CreateUserWrongEmail() {
        UserDto userDto = UserDto.builder().email("").name("User1").build();
        ItemRequestDto.builder()
                .description("").build();
        final ConstraintViolationException exception = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> service.createUser(userDto));

        Assertions.assertEquals("check.t.email: must not be empty", exception.getMessage());
    }

    @Test
    void test7UpdateWrongUser() {
        UserDto userDto = UserDto.builder().email("").name("User1").build();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("").build();
        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateUser(99L, userDto));

        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void test8GetWrongUser() {
        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> service.getUser(99L));

        Assertions.assertEquals("User not found", exception.getMessage());
    }


}
