package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTests {

    @Test
    public void toUserDto() {
        User user = new User();
        user.setId(1);
        user.setName("name");
        user.setEmail("e@mail.com");
        UserDto userDto = UserMapper.toUserDto(user);

        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    public void toUser() {
        UserDto userDto = new UserDto(1, "name", "e@mail.com");
        UserMapper userMapper = new UserMapper();
        User user = userMapper.toUser(userDto);

        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }
}

