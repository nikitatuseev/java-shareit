package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTests {

    @Test
    public void createUser() {
        int id = 1;
        String name = "name";
        String email = "e@mail.com";
        User user = new User(id, name, email);

        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
    }
}
