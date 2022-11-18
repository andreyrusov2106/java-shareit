package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private static long currentId;
    private static final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item createItem(Item item) {
        currentId++;
        item.setId(currentId);
        items.put(currentId, item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Item getItem(Long id) {
        return items.get(id);
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public boolean removeItem(Long id) {
        return items.remove(id) != null;
    }

    @Override
    public Boolean contains(Item item) {
        return items.containsKey(item.getId());
    }

    @Override
    public Boolean contains(Long id) {
        return items.containsKey(id);
    }
}
