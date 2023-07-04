package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto getById(int id);

    UserDto update(int id, UserDto userDto);

    void deleteById(int id);

    List<UserDto> getAll();
}
