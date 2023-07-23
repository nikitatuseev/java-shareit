package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    void create() throws Exception {
        int userId = 1;

        UserDto userDto = UserDto.builder()
                .name("name")
                .email("e@mail.com")
                .build();

        UserDto expectedUserDto = UserDto.builder()
                .id(userId)
                .name("n")
                .email("e@mail.com")
                .build();

        when(userService.create(any(UserDto.class))).thenReturn(expectedUserDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(expectedUserDto.getName())))
                .andExpect(jsonPath("$.email", is(expectedUserDto.getEmail())));
    }


    @Test
    void createUserDtoEmailNull() throws Exception {
        int userId = 1;
        UserDto userDto = UserDto.builder()
                .name("name")
                .build();

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    void createUserDtoEmailNotValid_() throws Exception {
        int userId = 1;
        UserDto userDto = UserDto.builder()
                .name("name")
                .email("mail")
                .build();

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    void getById() throws Exception {
        int userId = 1;
        UserDto expectedResult = UserDto.builder()
                .id(userId)
                .name("name")
                .email("e@mail.com")
                .build();

        when(userService.getById(userId)).thenReturn(expectedResult);

        mvc.perform(get("/users/{id}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedResult.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(expectedResult.getName())))
                .andExpect(jsonPath("$.email", is(expectedResult.getEmail())));
    }

    @Test
    void update() throws Exception {
        int userId = 1;
        UserDto updateBody = UserDto.builder()
                .name("name")
                .email("e@mail.com")
                .build();

        UserDto expectedResult = UserDto.builder()
                .id(userId)
                .name("name")
                .email("e@mail.com")
                .build();

        when(userService.update(anyInt(), any(UserDto.class))).thenReturn(expectedResult);

        mvc.perform(patch("/users/{id}", userId)
                        .content(mapper.writeValueAsString(updateBody))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedResult.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(expectedResult.getName())))
                .andExpect(jsonPath("$.email", is(expectedResult.getEmail())));
    }

    @Test
    void updateUserDtoEmailNotValid() throws Exception {
        int userId = 1;
        UserDto updateBody = UserDto.builder()
                .name("name")
                .email("mail")
                .build();

        mvc.perform(patch("/users/{id}", userId)
                .content(mapper.writeValueAsString(updateBody))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", userId)
                .accept(MediaType.APPLICATION_JSON));

    }

    @Test
    void delete() throws Exception {
        int userId = 1;
        mvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getAll() throws Exception {
        int userId = 1;

        UserDto userDto = UserDto.builder()
                .id(userId)
                .name("name")
                .email("e@mail.com")
                .build();

        when(userService.getAll()).thenReturn(List.of(userDto));

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(userDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$[0].email", is(userDto.getEmail())));
    }
}