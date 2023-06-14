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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final ItemMapper itemMapper;
    private final UserDao userDao;

    @Override
    public ItemDto create(int ownerId, ItemDto itemDto) {
        checkUserExist(ownerId);
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(userDao.getById(ownerId));
        Item itemCreated = itemDao.save(item);

        log.info("добавлен  item: {}", itemCreated);
        return itemMapper.toItemDto(itemCreated);
    }

    private void checkUserExist(int userId) {
        if (userDao.getById(userId) == null) {
            throw new NotFoundException("пользователь с id " + userId + " не найден");
        }
    }

    @Override
    public ItemDto getById(int userId, int id) {
        checkUserExist(userId);
        if (itemDao.getById(id) == null) {
            throw new NotFoundException("item с id " + id + " не найден");
        }

        return itemMapper.toItemDto(itemDao.getById(id));
    }

    @Override
    public ItemDto update(int ownerId, int id, ItemDto itemDto) {
        Item item = itemDao.getById(id);

        checkUserExist(ownerId);

        checkConditionsForUpdate(item, ownerId, id, itemDto);

        Item itemUpdated = itemDao.update(item);

        return itemMapper.toItemDto(itemUpdated);
    }

    private void checkConditionsForUpdate(Item item, int ownerId, int id, ItemDto itemDto) {
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
    }

    @Override
    public List<ItemDto> getAllByOwnerId(int ownerId) {
        checkUserExist(ownerId);
        return itemDao.getAllByOwnerId(ownerId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getAllByNameOrDescription(int userId, String substring) {
        checkUserExist(userId);
        return itemDao.getAllByNameOrDescription(substring).stream()
                .filter(Item::getAvailable)
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
