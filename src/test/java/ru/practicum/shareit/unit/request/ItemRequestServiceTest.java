package ru.practicum.shareit.unit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validator.Validator;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private Validator<ItemRequestDto> itemDtoValidator;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    private ItemRequestService itemRequestService;

    @BeforeEach
    void setUp() {
        itemRequestService = new ItemRequestServiceImpl(
                itemRequestRepository,
                userRepository,
                itemRepository,
                itemDtoValidator);
    }

    @Test
    public void testCreateItemRequestWrongOwner() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().build();

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());


        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> itemRequestService.createItemRequest(itemRequestDto, 100L));

        Assertions.assertEquals("User with id=100 not found", exception.getMessage());

    }

    @Test
    public void testGetItemRequestWrongOwner() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());


        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> itemRequestService.getItemRequest(100L, 100L));

        Assertions.assertEquals("User with id=100 not found", exception.getMessage());

    }

    @Test
    public void testGetItemRequestWrongId() {
        User u = new User(1L, "User1", "iuser1@mail.ru");
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(u));


        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> itemRequestService.getItemRequest(100L, 100L));

        Assertions.assertEquals("itemRequest with id not found100", exception.getMessage());

    }

    @Test
    public void testGetAllItemRequestWrongId() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> itemRequestService.getAllItemRequest(100L));
        Assertions.assertEquals("User with id=100 not found", exception.getMessage());

    }

    @Test
    public void testGetAllItemRequestWithPaginationWrongId() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        final ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> itemRequestService.getAllItemRequestWithPagination(100L, 0, 2));

        Assertions.assertEquals("User with id=100 not found", exception.getMessage());
    }
}
