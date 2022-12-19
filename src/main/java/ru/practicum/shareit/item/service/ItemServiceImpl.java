package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validator.Validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final Validator<ItemDto> itemDtoValidator;
    private final Validator<CommentDto> commentDtoValidator;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        Item newItem = new Item();
        ItemMapper.toItem(newItem, itemDto);
        itemDtoValidator.check(itemDto);
        var owner = userRepository.findById(userId);
        if (owner.isEmpty()) {
            throw new ResourceNotFoundException(String.format("User with id=%d not found", userId));
        } else {
            newItem.setOwner(owner.get());
        }
        if (itemDto.getRequestId() != null) {
            var itemRequest = itemRequestRepository.findById(itemDto.getRequestId());
            if (!itemRequest.isEmpty()) {
                newItem.setRequest(itemRequest.get());
            }
        }
        Item createdItem = itemRepository.save(newItem);
        log.info("Item created" + createdItem);
        return ItemMapper.toItemDto(createdItem);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) {
        Item updatedItem;
        Item item = new Item(itemRepository.getReferenceById(itemId));
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new ResourceNotFoundException("User is not owner of item!");
        }
        ItemMapper.toItem(item, itemDto);
        itemDtoValidator.check(ItemMapper.toItemDto(item));
        updatedItem = itemRepository.save(item);
        log.info("Item updated" + updatedItem);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        return itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .map(this::addBookingsToItem)
                .map(this::addCommentsToItem)
                .sorted(Comparator.comparingLong(ItemDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItem(Long id, Long userId) {
        var item = itemRepository.findById(id);
        if (item.isPresent()) {
            var itemDto = ItemMapper.toItemDto(item.get());
            if (item.get().getOwner().getId().equals(userId)) {
                addBookingsToItem(itemDto);
            }
            addCommentsToItem(itemDto);
            return itemDto;
        } else {
            throw new ResourceNotFoundException("Item with id not found" + id);
        }
    }

    private ItemDto addBookingsToItem(ItemDto dto) {
        Item item = new Item();
        ItemMapper.toItem(item, dto);
        var lastBookings = bookingRepository.findTop1ByItemAndEndIsBeforeOrderByEndDesc(item, LocalDateTime.now());
        if (lastBookings != null) {
            dto.setLastBooking(BookingMapper.toBookingDto(lastBookings));
        }
        var nextBookings = bookingRepository.findTop1ByItemAndStartIsAfterOrderByStartDesc(item, LocalDateTime.now());
        if (nextBookings != null) {
            dto.setNextBooking(BookingMapper.toBookingDto(nextBookings));
        }
        return dto;
    }

    private ItemDto addCommentsToItem(ItemDto dto) {
        Item item = new Item();
        ItemMapper.toItem(item, dto);
        var comments = commentRepository.findAllByItem(item);
        if (comments != null) {
            dto.setComments(comments.stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList()));
        } else {
            dto.setComments(new ArrayList<>());
        }
        return dto;
    }

    @Override
    public void removeItem(Long id) {
        itemRepository.deleteById(id);
    }

    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Long userId, Long itemId) {
        Comment newComment = new Comment();
        CommentMapper.toComment(newComment, commentDto);
        newComment.setCreated(LocalDateTime.now());
        commentDtoValidator.check(commentDto);
        var owner = userRepository.findById(userId);
        if (owner.isEmpty()) {
            throw new ResourceNotFoundException(String.format("User with id=%d not found", userId));
        } else {
            newComment.setAuthor(owner.get());
        }
        var item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Item with id=%d not found", userId));
        } else {
            newComment.setItem(item.get());
        }
        Booking booking = bookingRepository.findTop1ByItemAndBookerAndEndIsBefore(item.get(), owner.get(), LocalDateTime.now());
        if (booking == null) {
            throw new BadRequestException(String.format("User with id=%d not owner of item id=%d", userId, itemId));
        }
        Comment createdComment = commentRepository.save(newComment);
        log.info("Comment created" + createdComment);
        return CommentMapper.toCommentDto(createdComment);
    }

}
