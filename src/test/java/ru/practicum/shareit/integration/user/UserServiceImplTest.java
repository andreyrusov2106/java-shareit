package ru.practicum.shareit.integration.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test1",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceImplTest {
    private final UserService service;

    @Test
    void testSaveUser() {
        UserDto userDto = UserDto.builder().id(1L).email("user1@mail.ru").name("User1").build();
        UserDto user = service.createUser(userDto);
        assertThat(user.getId(), notNullValue());
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
        assertThat(user.getName(), equalTo(userDto.getName()));
    }

    @Test
    void testUpdateUser() {
        UserDto userDto2 = UserDto.builder().email("user1@mail.ru").name("User1New").build();
        UserDto user = service.updateUser(1L, userDto2);
        assertThat(user.getId(), notNullValue());
        assertThat(user.getEmail(), equalTo(userDto2.getEmail()));
        assertThat(user.getName(), equalTo(userDto2.getName()));
    }
}
