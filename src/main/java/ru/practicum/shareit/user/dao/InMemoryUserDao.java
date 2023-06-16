package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.UserException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemoryUserDao implements UserDao {
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private int generatedId = 1;

    @Override
    public User save(User user) {
        if (emailExist(user.getEmail())) {
            throw new UserException("email " + user.getEmail() + " уже существует");
        }
        user.setId(generatedId++);
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public User getById(int id) {
        return users.get(id);
    }

    @Override
    public User update(User user, String oldEmail) {
        emails.remove(oldEmail);
        emails.add(user.getEmail());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteById(int id) {
        emails.remove(users.get(id).getEmail());
        users.remove(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean emailExist(String email) {
        return emails.contains(email);
    }
}
