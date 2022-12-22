package ru.practicum.shareit.unit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.validator.UserValidator;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userValidator);
    }

    @Test
    public void testCreateUser() {
        UserDto udto = UserDto.builder().id(1L).email("iuser1@mail.ru").name("User1").build();
        User u = new User(1L, "User1", "iuser1@mail.ru");
        Mockito
                .when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(u);

        UserDto userDto = userService.createUser(udto);
        Assertions.assertEquals(userDto, udto);
    }


    @Test
    public void testUpdateUser() {
        UserDto udto = UserDto.builder().id(1L).email("iuser1@mail.ru").name("User1").build();
        User u = new User(1L, "User1", "iuser1@mail.ru");
        Mockito
                .when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(u);

        UserDto userDto = userService.createUser(udto);
        Assertions.assertEquals(userDto, udto);
    }
}
