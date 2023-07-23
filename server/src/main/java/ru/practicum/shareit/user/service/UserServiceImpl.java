package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        User userCreated = userRepository.save(user);

        log.info("Создан пользователь: {}", userCreated);
        return UserMapper.toUserDto(userCreated);
    }

    @Override
    public UserDto getById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return UserMapper.toUserDto(user);
    }


    @Transactional
    @Override
    public UserDto update(int id, UserDto userDto) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));

        if (userDto.getName() != null) {
            userToUpdate.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userToUpdate.setEmail(userDto.getEmail());
        }

        User userUpdated = userRepository.save(userToUpdate);

        log.info("Обновлен пользователь: {}", userUpdated);
        return UserMapper.toUserDto(userUpdated);
    }


    @Transactional
    @Override
    public void deleteById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("пользователь с id " + id + " не найден");
        }

        userRepository.deleteById(id);

        log.info("удалён пользователь: {}", user);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}