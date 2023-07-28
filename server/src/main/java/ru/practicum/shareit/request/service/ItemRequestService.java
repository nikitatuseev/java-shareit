package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(int userId, NewRequestDto creationRequestDto);

    ItemRequestDto getById(int userId, int requestId);

    List<ItemRequestDto> getAllByUser(int userId);

    List<ItemRequestDto> getAll(int userId, int from, int size);
}