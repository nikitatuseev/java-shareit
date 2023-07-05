package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(int ownerId, ItemDto itemDto);

    ItemDto getById(int userId, int id);

    ItemDto update(int ownerId, int id, ItemDto itemDto);

    List<ItemDto> getAllByOwnerId(int ownerId);

    List<ItemDto> getAllByNameOrDescription(int userId, String substring);

    CommentDto createComment(int userId, int itemId, CreateCommentDto creatCommentDto);
}
