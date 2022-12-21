package ru.practicum.shareit.unit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.validator.UserValidator;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    UserValidator userValidator;

    @Test
    public void testCreateUser() {

        UserServiceImpl userService = new UserServiceImpl(userRepository, userValidator);
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

        UserServiceImpl userService = new UserServiceImpl(userRepository, userValidator);
        UserDto udto = UserDto.builder().id(1L).email("iuser1@mail.ru").name("User1").build();
        User u = new User(1L, "User1", "iuser1@mail.ru");
        Mockito
                .when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(u);

        UserDto userDto = userService.createUser(udto);

        Assertions.assertEquals(userDto, udto);
    }
}
