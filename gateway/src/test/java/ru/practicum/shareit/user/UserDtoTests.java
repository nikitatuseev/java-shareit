package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserDtoTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void createUserDto() {

        int id = 1;
        String name = "name";
        String email = "e@mail.com";
        UserDto userDto = UserDto.builder()
                .id(id)
                .name(name)
                .email(email)
                .build();

        assertEquals(id, userDto.getId());
        assertEquals(name, userDto.getName());
        assertEquals(email, userDto.getEmail());
    }

    @Test
    public void createUserDtoInvalidName() {
        String name = "";
        String email = "e@mail.com";
        UserDto userDto = UserDto.builder()
                .name(name)
                .email(email)
                .build();

        assertDoesNotThrow(() -> {
            Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
            assertFalse(violations.isEmpty());
        });
    }

    @Test
    public void createUserDtoInvalidEmail() {
        String name = "name";
        String email = "mail";
        UserDto userDto = UserDto.builder()
                .name(name)
                .email(email)
                .build();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(!violations.isEmpty());
    }
}

