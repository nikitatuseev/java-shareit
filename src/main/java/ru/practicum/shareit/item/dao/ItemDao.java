package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {
    Item save(Item item);

    Item getById(int id);

    Item update(Item item);

    List<Item> getAllByOwnerId(int ownerId);

    List<Item> getAllByNameOrDescription(String substring);
}
