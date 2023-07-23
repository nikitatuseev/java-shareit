package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    private UserClient userClient;

    @Autowired
    private MockMvc mvc;

    @Test
    void create() throws Exception {
        int userId = 1;

        UserDto userDto = UserDto.builder()
                .name("name")
                .email("n@mail.com")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", Integer.toString(userId));
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "*/*");

        ResponseEntity<Object> expectedResult = new ResponseEntity<>(
                "response body from server",
                headers,
                HttpStatus.OK
        );

        when(userClient.create(any(UserDto.class)))
                .thenReturn(expectedResult);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createUserDtoNameNull() throws Exception {
        int userId = 1;

        UserDto userDto = UserDto.builder()
                .email("n@mail.com")
                .build();

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(userClient, never()).create(any(UserDto.class));
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
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(userClient, never()).create(any(UserDto.class));
    }

    @Test
    void createUserDtoEmailNotValid() throws Exception {
        int userId = 1;

        UserDto userDto = UserDto.builder()
                .name("name")
                .email("email")
                .build();

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(userClient, never()).create(any(UserDto.class));
    }

    @Test
    void getById() throws Exception {
        int userId = 1;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", Integer.toString(userId));
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "*/*");

        ResponseEntity<Object> expectedResult = new ResponseEntity<>(
                "response body from server",
                headers,
                HttpStatus.OK
        );

        when(userClient.getById(userId))
                .thenReturn(expectedResult);

        mvc.perform(get("/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        int userId = 1;

        UserDto updateBody = UserDto.builder()
                .name("name")
                .email("n@mail.com")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", Integer.toString(userId));
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "*/*");

        ResponseEntity<Object> expectedResult = new ResponseEntity<>(
                "response body from server",
                headers,
                HttpStatus.OK
        );

        when(userClient.update(anyInt(), any(UserDto.class)))
                .thenReturn(expectedResult);

        mvc.perform(patch("/users/{userId}", userId)
                        .content(mapper.writeValueAsString(updateBody))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void delete() throws Exception {
        int userId = 1;

        mvc.perform(MockMvcRequestBuilders.delete("/users/{userId}", userId)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userClient, times(1)).delete(userId);
        verifyNoMoreInteractions(userClient);
    }

    @Test
    void getAll() throws Exception {
        int userId = 1;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", Integer.toString(userId));
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "*/*");

        ResponseEntity<Object> expectedResult = new ResponseEntity<>(
                "response body from server",
                headers,
                HttpStatus.OK
        );

        when(userClient.getAll())
                .thenReturn(expectedResult);

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}