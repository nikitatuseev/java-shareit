package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.toItemDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserDao userDao;

    @Override
    public ItemDto create(int ownerId, ItemDto itemDto) {
        User owner = userDao.getById(ownerId);
        if (owner == null) {
            throw new NotFoundException("Пользователь с ID " + ownerId + " не найден");
        }

        Item item = ItemMapper.toItem(itemDto, owner);
        Item itemCreated = itemDao.save(item);

        log.info("Добавлен item: {}", itemCreated);
        return toItemDto(itemCreated, owner);
    }

    private void checkUserExist(int userId) {
        if (userDao.getById(userId) == null) {
            throw new NotFoundException("пользователь с id " + userId + " не найден");
        }
    }

    @Override
    public ItemDto getById(int userId, int id) {
        checkUserExist(userId);
        Item item = itemDao.getById(id);
        if (item == null) {
            throw new NotFoundException("Item с ID " + id + " не найден");
        }

        return toItemDto(item, item.getOwner());
    }

    @Override
    public ItemDto update(int ownerId, int id, ItemDto itemDto) {
        checkUserExist(ownerId);

        Item item = itemDao.getById(id);
        if (item == null || item.getOwner().getId() != ownerId) {
            throw new NotFoundException("Item с id " + id + " не найден или не принадлежит пользователю с id " + ownerId);
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        User owner = userDao.getById(ownerId);
        item.setOwner(owner);

        Item itemUpdated = itemDao.update(item);

        return toItemDto(itemUpdated, owner);
    }

    @Override
    public List<ItemDto> getAllByOwnerId(int ownerId) {
        checkUserExist(ownerId);
        User owner = userDao.getById(ownerId);
        if (owner == null) {
            throw new NotFoundException("Пользователь с ID " + ownerId + " не найден");
        }

        return itemDao.getAllByOwnerId(ownerId).stream()
                .map(item -> toItemDto(item, owner))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getAllByNameOrDescription(int userId, String substring) {
        checkUserExist(userId);
        User user = userDao.getById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }

        return itemDao.getAllByNameOrDescription(substring).stream()
                .filter(Item::getAvailable)
                .map(item -> toItemDto(item, user))
                .collect(Collectors.toList());
    }
}
