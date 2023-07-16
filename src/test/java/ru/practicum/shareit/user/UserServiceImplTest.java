package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void createWithValidUserDto() {
        UserDto userDto = new UserDto();
        userDto.setName("Name name");
        userDto.setEmail("name@yandex.com");

        User user = new User();
        user.setId(1);
        user.setName("Name name");
        user.setEmail("name@yandex.com");

        when(userMapper.toUser(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        UserDto result = userService.create(userDto);

        assertNotNull(result);
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    public void getByIdWithValidId() {
        int userId = 1;

        User user = new User();
        user.setId(userId);
        user.setName("Name name");
        user.setEmail("name@yandex.com");

        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setId(userId);
        expectedUserDto.setName("Name name");
        expectedUserDto.setEmail("name@yandex.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto result = userService.getById(userId);

        assertNotNull(result);
        assertEquals(expectedUserDto.getId(), result.getId());
        assertEquals(expectedUserDto.getName(), result.getName());
        assertEquals(expectedUserDto.getEmail(), result.getEmail());
    }

    @Test
    public void updateWithValidIdAndUserDto() {
        int userId = 1;

        UserDto userDto = new UserDto();
        userDto.setName("Name name");
        userDto.setEmail("name@yandex.com");

        User userToUpdate = new User();
        userToUpdate.setId(userId);
        userToUpdate.setName("Name");
        userToUpdate.setEmail("name@yandex.com");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName("Name");
        updatedUser.setEmail("name@yandex.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userToUpdate));
        when(userRepository.save(userToUpdate)).thenReturn(updatedUser);

        UserDto result = userService.update(userId, userDto);

        assertNotNull(result);
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    public void deleteByIdWithValidId() {
        int userId = 1;

        User user = new User();
        user.setId(userId);
        user.setName("Name name");
        user.setEmail("name@yandex.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteById(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    public void getAll() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId(1);
        user1.setName("Name name");
        user1.setEmail("name@yandex.com");
        users.add(user1);

        User user2 = new User();
        user2.setId(2);
        user2.setName("Second user");
        user2.setEmail("second@yandex.com");
        users.add(user2);

        List<UserDto> expectedUserDtos = new ArrayList<>();
        UserDto userDto1 = new UserDto();
        userDto1.setId(1);
        userDto1.setName("Name name");
        userDto1.setEmail("name@yandex.com");
        expectedUserDtos.add(userDto1);

        UserDto userDto2 = new UserDto();
        userDto2.setId(2);
        userDto2.setName("Second user");
        userDto2.setEmail("second@yandex.com");
        expectedUserDtos.add(userDto2);

        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> result = userService.getAll();

        assertEquals(expectedUserDtos.size(), result.size());
    }
}


