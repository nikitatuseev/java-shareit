package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.EmailException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.UserException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserMapper userMapper;

    @Override
    public UserDto create(UserDto userDto) {
        if (userDto.getEmail().contains("@")) {
            emailUniqueCheck(userDto.getEmail());

            User user = userMapper.toUser(userDto);
            User userCreated = userDao.save(user);

            log.info("создан пользователь: {}", userCreated);
            return userMapper.toUserDto(userCreated);
        } else {
            throw new EmailException("неверный email");
        }
    }

    @Override
    public UserDto getById(int id) {
        if (userDao.getById(id) == null) {
            throw new NotFoundException("пользователь с id " + id + " не найден");
        }

        return userMapper.toUserDto(userDao.getById(id));
    }

    @Override
    public UserDto update(int id, UserDto userDto) {
        User user = userDao.getById(id);

        if (user == null) {
            throw new NotFoundException("пользователь с id " + id + " не найден");
        }

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !user.getEmail().equals(userDto.getEmail())) {
            emailUniqueCheck(userDto.getEmail());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        User userUpdated = userDao.update(user);

        log.info("обновлён пользователь: {}", userUpdated);
        return userMapper.toUserDto(userUpdated);
    }

    @Override
    public void deleteById(int id) {
        User user = userDao.getById(id);
        if (user == null) {
            throw new NotFoundException("пользователь с id " + id + " не найден");
        }

        userDao.deleteById(id);

        log.info("удалён пользователь: {}", user);
    }

    @Override
    public List<UserDto> getAll() {
        return userDao.getAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    private void emailUniqueCheck(String email) {
        if (userDao.emailExist(email)) {
            throw new UserException("email " + email + " уже существует");
        }
    }
}