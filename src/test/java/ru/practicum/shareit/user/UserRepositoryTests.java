package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void existsByEmail() {
        User user = new User();
        user.setName("name");
        user.setEmail("e@mail.com");
        userRepository.save(user);
        boolean exists = userRepository.existsByEmail("e@mail.com");

        assertTrue(exists);
    }

    @Test
    public void emailDoesNotExist() {
        boolean exists = userRepository.existsByEmail("e@mail.com");

        assertFalse(exists);
    }
}
