package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemDao implements ItemDao {
    private final Map<Integer, Item> items = new HashMap<>();
    private final Map<Integer, List<Item>> ownerItemsMap = new HashMap<>();
    private int generatedId = 1;

    @Override
    public Item save(Item item) {
        item.setId(generatedId++);
        items.put(item.getId(), item);
        addItem(item);
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

    private void addItem(Item item) {
        int ownerId = item.getOwner().getId();

        // Проверяем наличие списка предметов для данного владельца
        if (!ownerItemsMap.containsKey(ownerId)) {
            ownerItemsMap.put(ownerId, new ArrayList<>());
        }

        // Добавляем предмет в список для данного владельца
        ownerItemsMap.get(ownerId).add(item);
    }

    @Override
    public List<Item> getAllByOwnerId(int ownerId) {
        // Возвращаем пустой список, если нет предметов для данного владельца
        return ownerItemsMap.getOrDefault(ownerId, Collections.emptyList());
    }

    @Override
    public List<Item> getAllByNameOrDescription(String string) {
        String stringLowerCase = string.toLowerCase();
        if (string.equals("")) {
            return List.of();
        }
        return items.values().stream()
                .filter(item -> item.getAvailable() &&
                        (item.getName().toLowerCase().contains(stringLowerCase)
                                || item.getDescription().toLowerCase().contains(stringLowerCase)))
                .collect(Collectors.toList());
    }
}
