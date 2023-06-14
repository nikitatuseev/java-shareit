package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    User save(User user);

    User getById(int id);

    User update(User user);

    void deleteById(int id);

    List<User> getAll();

    boolean emailExist(String email);
}
