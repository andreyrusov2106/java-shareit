package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validator.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final Validator<ItemDto> itemDtoValidator;

    @Autowired
    public ItemService(ItemRepository itemRepository, Validator<ItemDto> itemDtoValidator, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.itemDtoValidator = itemDtoValidator;
        this.userRepository = userRepository;
    }

    public ItemDto createItem(ItemDto itemDto, Long userId) {
        Item newItem = new Item();
        ItemMapper.toItem(newItem, itemDto);
        itemDtoValidator.check(itemDto);
        User owner = userRepository.getUser(userId);
        if (owner == null) {
            throw new ResourceNotFoundException(String.format("User with id=%d not found", userId));
        } else {
            newItem.setOwner(owner);
        }
        Item createdItem = itemRepository.createItem(newItem);
        log.info("Item created" + createdItem);
        return ItemMapper.toItemDto(createdItem);
    }

    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) {
        Item updatedItem;
        if (itemRepository.contains(itemId)) {
            Item item = new Item(itemRepository.getItem(itemId));
            if (!Objects.equals(item.getOwner().getId(), userId)) {
                throw new ResourceNotFoundException("User is not owner of item!");
            }
            ItemMapper.toItem(item, itemDto);
            itemDtoValidator.check(ItemMapper.toItemDto(item));
            updatedItem = itemRepository.updateItem(item);
            log.info("User updated" + updatedItem);
        } else {
            throw new ResourceNotFoundException("Item not found" + userId);
        }
        return ItemMapper.toItemDto(updatedItem);
    }

    public List<ItemDto> getAllItemsByUserId(Long userId) {
        return itemRepository.getAllItems()
                .stream()
                .filter(i -> Objects.equals(i.getOwner().getId(), userId))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public ItemDto getItem(Long id) {
        if (itemRepository.contains(id)) {
            return ItemMapper.toItemDto(itemRepository.getItem(id));
        } else {
            throw new ResourceNotFoundException("Item with id not found" + id);
        }
    }

    public List<ItemDto> searchItemsByDescription(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.getAllItems()
                .stream()
                .filter(i -> i.getDescription().toLowerCase().contains(text.toLowerCase()) && i.getAvailable())
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public void removeItem(Long id) {
        if (!itemRepository.removeItem(id)) {
            throw new ResourceNotFoundException("Item not found");
        }
    }
}
