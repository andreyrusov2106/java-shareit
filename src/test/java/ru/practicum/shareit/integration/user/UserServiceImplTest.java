package ru.practicum.shareit.integration.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import javax.persistence.EntityManager;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
public class UserServiceImplTest {
    private final UserService service;
    private final EntityManager em;

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
        UserDto userDto = UserDto.builder().id(1L).email("user1@mail.ru").name("User1").build();
        UserDto user = service.createUser(userDto);
        UserDto userDto2 = UserDto.builder().email("user1@mail.ru").name("User1New").build();
        user = service.updateUser(1L,userDto);
        assertThat(user.getId(), notNullValue());
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
        assertThat(user.getName(), equalTo(userDto.getName()));
    }
}
