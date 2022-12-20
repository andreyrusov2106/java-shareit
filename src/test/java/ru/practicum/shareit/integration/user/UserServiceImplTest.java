package ru.practicum.shareit.integration.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test1",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class UserServiceImplTest {
    private final UserService service;

    @Test
    @Order(1)
    void testSaveUser() {
        UserDto userDto = UserDto.builder().id(1L).email("user1@mail.ru").name("User1").build();
        UserDto user = service.createUser(userDto);
        assertThat(user.getId(), notNullValue());
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
        assertThat(user.getName(), equalTo(userDto.getName()));
    }

    @Test
    @Order(2)
    void testUpdateUser() {
        UserDto userDto2 = UserDto.builder().email("user1@mail.ru").name("User1New").build();
        UserDto user = service.updateUser(1L, userDto2);
        assertThat(user.getId(), notNullValue());
        assertThat(user.getEmail(), equalTo(userDto2.getEmail()));
        assertThat(user.getName(), equalTo(userDto2.getName()));
    }

    @Test
    @Order(3)
    void testGetAllUsers() {
        List<UserDto> users = service.getAllUsers();
        assertThat(users.size(), equalTo(1));

    }

    @Test
    @Order(4)
    void testGetUser() {
        UserDto userDto2 = UserDto.builder().email("user1@mail.ru").name("User1New").build();
        UserDto user = service.getUser(1L);
        assertThat(user.getId(), notNullValue());
        assertThat(user.getEmail(), equalTo(userDto2.getEmail()));
        assertThat(user.getName(), equalTo(userDto2.getName()));
    }

    @Test
    @Order(5)
    void testRemoveUser() {
        service.removeUser(1L);
        List<UserDto> users = service.getAllUsers();
        assertThat(users.size(), equalTo(0));
    }
}
