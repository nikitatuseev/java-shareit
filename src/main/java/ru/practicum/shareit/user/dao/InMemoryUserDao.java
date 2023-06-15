package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemoryUserDao implements UserDao {
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, String> emails = new HashMap<>();
    private int generatedId = 1;

    @Override
    public User save(User user) {
        user.setId(generatedId++);
        emails.put(user.getId(), user.getEmail());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getById(int id) {
        return users.get(id);
    }

    @Override
    public User update(User user) {
        emails.put(user.getId(), user.getEmail());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteById(int id) {
        emails.remove(users.get(id).getId());
        users.remove(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean emailExist(String email) {
        return emails.containsValue(email);
    }
}
