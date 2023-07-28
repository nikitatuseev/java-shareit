package ru.practicum.shareit.itemRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;
    @MockBean
    private ItemRequestService requestService;
    @Autowired
    private MockMvc mvc;

    @Test
    void create() throws Exception {
        int userId = 1;

        NewRequestDto creationRequestDto = NewRequestDto.builder()
                .description("text")
                .build();

        ItemRequestDto expectedResult = ItemRequestDto.builder()
                .id(1)
                .description("text")
                .created(LocalDateTime.now())
                .requestorId(1)
                .items(Collections.emptyList())
                .build();

        when(requestService.create(anyInt(), any(NewRequestDto.class))).thenReturn(expectedResult);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(creationRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedResult.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(expectedResult.getDescription())))
                .andExpect(jsonPath("$.requestorId", is(expectedResult.getRequestorId()), Integer.class))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    void getById() throws Exception {
        int userId = 1;
        int requestId = 1;

        ItemRequestDto expectedResult = ItemRequestDto.builder()
                .id(requestId)
                .description("text")
                .created(LocalDateTime.now())
                .requestorId(1)
                .items(Collections.emptyList())
                .build();

        when(requestService.getById(anyInt(), anyInt())).thenReturn(expectedResult);

        mvc.perform(get("/requests/{id}", requestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedResult.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(expectedResult.getDescription())))
                .andExpect(jsonPath("$.requestorId", is(expectedResult.getRequestorId()), Integer.class))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    void getAllByUser() throws Exception {
        int userId = 1;
        ItemRequestDto expectedResult = ItemRequestDto.builder()
                .id(1)
                .description("text")
                .created(LocalDateTime.of(2022, 2, 2, 2, 2))
                .requestorId(1)
                .items(Collections.emptyList())
                .build();

        when(requestService.getAllByUser(anyInt())).thenReturn(List.of(expectedResult));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(expectedResult.getId()), Integer.class))
                .andExpect(jsonPath("$[0].description", is(expectedResult.getDescription())))
                .andExpect(jsonPath("$[0].requestorId", is(expectedResult.getRequestorId()), Integer.class))
                .andExpect(jsonPath("$[0].items", hasSize(0)));
    }

    @Test
    void getAll() throws Exception {
        int userId = 1;
        ItemRequestDto expectedResult = ItemRequestDto.builder()
                .id(1)
                .description("text")
                .created(LocalDateTime.of(222, 2, 2, 2, 2))
                .requestorId(1)
                .items(Collections.emptyList())
                .build();

        when(requestService.getAll(anyInt(), anyInt(), anyInt())).thenReturn(List.of(expectedResult));

        mvc.perform(get("/requests/all?from=0&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(expectedResult.getId()), Integer.class))
                .andExpect(jsonPath("$[0].description", is(expectedResult.getDescription())))
                .andExpect(jsonPath("$[0].requestorId", is(expectedResult.getRequestorId()), Integer.class))
                .andExpect(jsonPath("$[0].items", hasSize(0)));
    }

    @Test
    void getAllNotValid() throws Exception {
        int userId = 1;
        mvc.perform(get("/requests/all?from=-1&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON));

        mvc.perform(get("/requests/all?from=0&size=0")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON));

        mvc.perform(get("/requests/all?from=1&size=0")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON));

        mvc.perform(get("/requests/all?from=0&size=-1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON));
    }
}

