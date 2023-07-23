package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ItemClient itemClient;

    @Autowired
    private MockMvc mvc;

    @Test
    void create() throws Exception {
        int userId = 1;

        ItemDto inputItemDto = ItemDto.builder()
                .name("name")
                .description("text")
                .available(true)
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

        when(itemClient.create(anyInt(), any(ItemDto.class)))
                .thenReturn(expectedResult);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(inputItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createNameNull() throws Exception {
        int userId = 1;

        ItemDto itemDto = ItemDto.builder()
                .description("text")
                .available(true)
                .build();

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(itemClient, never()).create(anyInt(), any(ItemDto.class));
    }


    @Test
    void createDescriptionNull() throws Exception {
        int userId = 1;

        ItemDto itemDto = ItemDto.builder()
                .name("name")
                .available(true)
                .build();

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(itemClient, never()).create(anyInt(), any(ItemDto.class));
    }


    @Test
    void createAvailableNull() throws Exception {
        int userId = 1;

        ItemDto itemDto = ItemDto.builder()
                .name("name")
                .description("text")
                .build();

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(itemClient, never()).create(anyInt(), any(ItemDto.class));
    }

    @Test
    void getById() throws Exception {
        int userId = 1;
        int itemId = 1;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", Integer.toString(userId));
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "*/*");

        ResponseEntity<Object> expectedResult = new ResponseEntity<>(
                "response body from server",
                headers,
                HttpStatus.OK
        );

        when(itemClient.getById(userId, itemId))
                .thenReturn(expectedResult);

        mvc.perform(get("/items/{itemId}", itemId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        int userId = 1;
        int itemId = 1;

        ItemDto updateBody = ItemDto.builder()
                .name("name")
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

        when(itemClient.update(anyInt(), anyInt(), any(ItemDto.class)))
                .thenReturn(expectedResult);

        mvc.perform(patch("/items/{itemId}", itemId)
                        .content(mapper.writeValueAsString(updateBody))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllByOwner() throws Exception {
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

        when(itemClient.getAllByOwnerId(userId, from, size))
                .thenReturn(expectedResult);

        mvc.perform(get("/items?from={from}&size={size}", from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createComment() throws Exception {
        int userId = 1;
        int itemId = 1;

        CreateCommentDto creationCommentDto = CreateCommentDto.builder()
                .text("text")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", Long.toString(userId));
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "*/*");

        ResponseEntity<Object> expectedResult = new ResponseEntity<>(
                "response body from server",
                headers,
                HttpStatus.OK
        );

        when(itemClient.createComment(anyInt(), anyInt(), any(CreateCommentDto.class)))
                .thenReturn(expectedResult);

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .content(mapper.writeValueAsString(creationCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createCommentTextNull() throws Exception {
        int userId = 1;
        int itemId = 1;

        CreateCommentDto creationCommentDto = CreateCommentDto.builder()
                .build();

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .content(mapper.writeValueAsString(creationCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(itemClient, never()).createComment(anyInt(), anyInt(), any(CreateCommentDto.class));
    }

}

