package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exeption.UserException;
import ru.practicum.shareit.exeption.ValidationException;
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

    @Test
    public void userException() {
        int id = 1;
        Assertions.assertThrows(UserException.class, () -> {
            if (id > 0) {
                throw new UserException("sds");
            }
        });
    }

    @Test
    public void validException() {
        int id = 1;
        Assertions.assertThrows(ValidationException.class, () -> {
            if (id > 0) {
                throw new ValidationException("sds");
            }
        });
    }
}
