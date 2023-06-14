package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemDao implements ItemDao {
    private final Map<Integer, Item> items = new HashMap<>();
    private int generatedId = 1;

    @Override
    public Item save(Item item) {
        item.setId(generatedId++);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getById(int id) {
        return items.get(id);
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> getAllByOwnerId(int ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getAllByNameOrDescription(String string) {
        String stringLowerCase = string.toLowerCase();
        if (string.equals("")) {
            return List.of();
        }
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(stringLowerCase))
                        || (item.getDescription().toLowerCase().contains(stringLowerCase)))
                .collect(Collectors.toList());
    }
}
