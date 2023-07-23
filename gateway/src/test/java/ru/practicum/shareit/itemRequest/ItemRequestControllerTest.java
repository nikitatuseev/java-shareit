package ru.practicum.shareit.itemRequest;

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
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.NewRequestDto;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;
    @MockBean
    private ItemRequestClient requestClient;
    @Autowired
    private MockMvc mvc;

    @Test
    void create() throws Exception {
        int userId = 1;

        NewRequestDto createRequestDto = NewRequestDto.builder()
                .description("text")
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

        when(requestClient.create(anyInt(), any(NewRequestDto.class)))
                .thenReturn(expectedResult);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(createRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createDescriptionNull() throws Exception {
        int userId = 1;

        NewRequestDto createRequestDto = NewRequestDto.builder()
                .build();

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(createRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(requestClient, never()).create(anyInt(), any(NewRequestDto.class));
    }

    @Test
    void getById() throws Exception {
        int userId = 1;
        int requestId = 1;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", Integer.toString(userId));
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "*/*");

        ResponseEntity<Object> expectedResult = new ResponseEntity<>(
                "response body from server",
                headers,
                HttpStatus.OK
        );

        when(requestClient.getById(userId, requestId))
                .thenReturn(expectedResult);

        mvc.perform(get("/requests/{requestId}", requestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllByUser() throws Exception {
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

        when(requestClient.getAllByUser(userId))
                .thenReturn(expectedResult);

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAll() throws Exception {
        int userId = 1;
        int from = 0;
        int size = 10;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", Integer.toString(userId));
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "*/*");

        ResponseEntity<Object> expectedResult = new ResponseEntity<>(
                "response body from server",
                headers,
                HttpStatus.OK
        );

        when(requestClient.getAll(userId, from, size))
                .thenReturn(expectedResult);

        mvc.perform(get("/requests/all?from={from}&size={size}", from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON));
    }
}

