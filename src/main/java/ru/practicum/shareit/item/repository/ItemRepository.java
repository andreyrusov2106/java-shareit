package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    Item createItem(Item item);

    Item updateItem(Item user);

    Item getItem(Long id);

    List<Item> getAllItems();

    boolean removeItem(Long id);

    Boolean contains(Item item);

    Boolean contains(Long id);
}
