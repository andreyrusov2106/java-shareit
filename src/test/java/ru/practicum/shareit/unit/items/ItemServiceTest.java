package ru.practicum.shareit.unit.items;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.validator.CommentValidator;
import ru.practicum.shareit.item.validator.ItemValidator;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    ItemRequestRepository itemRequestRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    ItemValidator itemValidator;
    @Mock
    CommentValidator commentValidator;

    @Test
    public void testCreateUItem() {

        ItemService itemService = new ItemServiceImpl(
                itemRepository,
                itemRequestRepository,
                userRepository,
                bookingRepository,
                commentRepository,
                itemValidator,
                commentValidator);
        ItemDto itemDto = ItemDto.builder().build();
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> itemService.createItem(itemDto, 1L));

        Assertions.assertEquals("User with id=1 not found", exception.getMessage());
    }

    @Test
    public void testUpdateItem() {

        ItemService itemService = new ItemServiceImpl(
                itemRepository,
                itemRequestRepository,
                userRepository,
                bookingRepository,
                commentRepository,
                itemValidator,
                commentValidator);
        ItemDto itemDto = ItemDto.builder().build();
        Item i = new Item();
        ItemMapper.toItem(i, itemDto);
        User u = new User(1L, "User1", "iuser1@mail.ru");
        i.setOwner(u);
        Mockito
                .when(itemRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(i);


        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> itemService.updateItem(itemDto, 1L, 100L));

        Assertions.assertEquals("User is not owner of item!", exception.getMessage());
    }
}
