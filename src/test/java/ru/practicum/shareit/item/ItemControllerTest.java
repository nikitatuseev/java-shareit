package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    @Test
    void create() throws Exception {
        int userId = 1;

        ItemDto expectedItemDto = ItemDto.builder()
                .id(1)
                .ownerId(1)
                .name("item")
                .description("text")
                .available(true)
                .lastBooking(null)
                .nextBooking(null)
                .requestId(1)
                .build();

        ItemDto inputItemDto = ItemDto.builder()
                .name("item")
                .description("text")
                .available(true)
                .build();

        when(itemService.create(anyInt(), any(ItemDto.class))).thenReturn(expectedItemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(inputItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.ownerId", is(expectedItemDto.getOwnerId()), Integer.class))
                .andExpect(jsonPath("$.name", is(expectedItemDto.getName())))
                .andExpect(jsonPath("$.description", is(expectedItemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(expectedItemDto.getAvailable())))
                .andExpect(jsonPath("$.lastBooking", is(expectedItemDto.getLastBooking())))
                .andExpect(jsonPath("$.nextBooking", is(expectedItemDto.getNextBooking())))
                .andExpect(jsonPath("$.requestId", is(expectedItemDto.getRequestId()), Integer.class));
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

        verify(itemService, never()).create(anyInt(), any(ItemDto.class));
    }

    @Test
    void createDescriptionNull() throws Exception {
        int userId = 1;

        ItemDto itemDto = ItemDto.builder()
                .name("item")
                .available(true)
                .build();

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(itemService, never()).create(anyInt(), any(ItemDto.class));
    }

    @Test
    void getById() throws Exception {
        int userId = 1;
        int itemId = 1;

        ItemDto expectedItemDto = ItemDto.builder()
                .id(1)
                .ownerId(1)
                .name("item")
                .description("text")
                .available(true)
                .lastBooking(null)
                .nextBooking(null)
                .requestId(1)
                .build();

        when(itemService.getById(anyInt(), anyInt())).thenReturn(expectedItemDto);

        mvc.perform(get("/items/{id}", itemId)
                .characterEncoding(StandardCharsets.UTF_8)
                .header("X-Sharer-User-Id", userId)
                .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateItemDtoValid() throws Exception {
        int userId = 1;
        int itemId = 1;

        ItemDto updateBody = ItemDto.builder()
                .name("item")
                .build();

        ItemDto expectedItemDto = ItemDto.builder()
                .id(1)
                .ownerId(1)
                .name("item")
                .description("text")
                .available(true)
                .lastBooking(null)
                .nextBooking(null)
                .requestId(1)
                .build();

        when(itemService.update(anyInt(), anyInt(), any(ItemDto.class))).thenReturn(expectedItemDto);

        mvc.perform(patch("/items/{id}", itemId)
                        .content(mapper.writeValueAsString(updateBody))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.ownerId", is(expectedItemDto.getOwnerId()), Integer.class))
                .andExpect(jsonPath("$.name", is(expectedItemDto.getName())))
                .andExpect(jsonPath("$.description", is(expectedItemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(expectedItemDto.getAvailable())))
                .andExpect(jsonPath("$.lastBooking", is(expectedItemDto.getLastBooking())))
                .andExpect(jsonPath("$.nextBooking", is(expectedItemDto.getNextBooking())))
                .andExpect(jsonPath("$.requestId", is(expectedItemDto.getRequestId()), Integer.class));
    }

    @Test
    void getAllByOwner() throws Exception {
        int userId = 1;

        ItemDto expectedItemDto = ItemDto.builder()
                .id(1)
                .ownerId(1)
                .name("item")
                .description("text")
                .available(true)
                .lastBooking(null)
                .nextBooking(null)
                .requestId(1)
                .build();

        when(itemService.getAllByOwnerId(anyInt(), anyInt(), anyInt())).thenReturn(List.of(expectedItemDto));

        mvc.perform(get("/items?from=0&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(expectedItemDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].ownerId", is(expectedItemDto.getOwnerId()), Integer.class))
                .andExpect(jsonPath("$[0].name", is(expectedItemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(expectedItemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(expectedItemDto.getAvailable())))
                .andExpect(jsonPath("$[0].lastBooking", is(expectedItemDto.getLastBooking())))
                .andExpect(jsonPath("$[0].nextBooking", is(expectedItemDto.getNextBooking())))
                .andExpect(jsonPath("$[0].requestId", is(expectedItemDto.getRequestId()), Integer.class));
    }

    @Test
    void getAllByOwnerFromOrSizeNotValid() throws Exception {
        int userId = 1;

        mvc.perform(get("/items?from=-1&size=10")
                .characterEncoding(StandardCharsets.UTF_8)
                .header("X-Sharer-User-Id", userId)
                .accept(MediaType.APPLICATION_JSON));

        mvc.perform(get("/items?from=0&size=0")
                .characterEncoding(StandardCharsets.UTF_8)
                .header("X-Sharer-User-Id", userId)
                .accept(MediaType.APPLICATION_JSON));

        mvc.perform(get("/items?from=1&size=0")
                .characterEncoding(StandardCharsets.UTF_8)
                .header("X-Sharer-User-Id", userId)
                .accept(MediaType.APPLICATION_JSON));

        mvc.perform(get("/items?from=0&size=-1")
                .characterEncoding(StandardCharsets.UTF_8)
                .header("X-Sharer-User-Id", userId)
                .accept(MediaType.APPLICATION_JSON));

    }

    @Test
    void getAllByNameOrDesc() throws Exception {
        int userId = 1;

        ItemDto expectedItemDto = ItemDto.builder()
                .id(1)
                .ownerId(1)
                .name("item")
                .description("text")
                .available(true)
                .lastBooking(null)
                .nextBooking(null)
                .requestId(1)
                .build();

        when(itemService.getAllByNameOrDescription(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(expectedItemDto));

        mvc.perform(get("/items/search?text=hAmm&from=0&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(expectedItemDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].ownerId", is(expectedItemDto.getOwnerId()), Integer.class))
                .andExpect(jsonPath("$[0].name", is(expectedItemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(expectedItemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(expectedItemDto.getAvailable())))
                .andExpect(jsonPath("$[0].lastBooking", is(expectedItemDto.getLastBooking())))
                .andExpect(jsonPath("$[0].nextBooking", is(expectedItemDto.getNextBooking())))
                .andExpect(jsonPath("$[0].requestId", is(expectedItemDto.getRequestId()), Integer.class));
    }

    @Test
    void getAllByNameOrDescFromOrSizeNotValid() throws Exception {
        int userId = 1;

        mvc.perform(get("/items/search?text=hAmm&from=-1&size=10")
                .characterEncoding(StandardCharsets.UTF_8)
                .header("X-Sharer-User-Id", userId)
                .accept(MediaType.APPLICATION_JSON));

        mvc.perform(get("/items/search?text=hAmm&from=0&size=0")
                .characterEncoding(StandardCharsets.UTF_8)
                .header("X-Sharer-User-Id", userId)
                .accept(MediaType.APPLICATION_JSON));

        mvc.perform(get("/items/search?text=hAmm&from=1&size=0")
                .characterEncoding(StandardCharsets.UTF_8)
                .header("X-Sharer-User-Id", userId)
                .accept(MediaType.APPLICATION_JSON));

        mvc.perform(get("/items/search?text=hAmm&from=0&size=-1")
                .characterEncoding(StandardCharsets.UTF_8)
                .header("X-Sharer-User-Id", userId)
                .accept(MediaType.APPLICATION_JSON));

    }

    @Test
    void createComment() throws Exception {
        int userId = 1;
        int itemId = 1;

        CreateCommentDto creationCommentDto = CreateCommentDto.builder()
                .text("text")
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("text")
                .authorName("name")
                .created(LocalDateTime.now())
                .build();

        when(itemService.createComment(anyInt(), anyInt(), any(CreateCommentDto.class))).thenReturn(commentDto);

        mvc.perform(post("/items/{id}/comment", itemId)
                        .content(mapper.writeValueAsString(creationCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Integer.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
    }

    @Test
    void createNullComment() throws Exception {
        int userId = 1;
        int itemId = 1;

        CreateCommentDto creationCommentDto = CreateCommentDto.builder()
                .build();

        mvc.perform(post("/items/{id}/comment", itemId)
                        .content(mapper.writeValueAsString(creationCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(itemService, never()).createComment(anyInt(), anyInt(), any(CreateCommentDto.class));
    }
}

