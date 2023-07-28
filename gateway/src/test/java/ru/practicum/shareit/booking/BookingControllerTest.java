package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;
    @MockBean
    private BookingClient bookingClient;
    @Autowired
    private MockMvc mvc;

    @Test
    void create() throws Exception {
        int userId = 1;

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
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

        when(bookingClient.create(userId, createBookingDto))
                .thenReturn(expectedResult);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(createBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createStartNull() throws Exception {
        int userId = 1;

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .itemId(1)
                .end(LocalDateTime.now().plusDays(1))
                .build();

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(createBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(bookingClient, never()).create(anyInt(), any(CreateBookingDto.class));
    }

    @Test
    void createEndNull() throws Exception {
        int userId = 1;

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusHours(1))
                .build();

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(createBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(bookingClient, never()).create(anyInt(), any(CreateBookingDto.class));
    }

    @Test
    void createEndBeforeNow() throws Exception {
        int userId = 1;

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().minusHours(1))
                .build();

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(createBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(bookingClient, never()).create(anyInt(), any(CreateBookingDto.class));
    }

    @Test
    void confirmStatus() throws Exception {
        int userId = 1;
        int bookingId = 1;
        boolean approved = true;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", Integer.toString(userId));
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "*/*");

        ResponseEntity<Object> expectedResult = new ResponseEntity<>(
                "response body from server",
                headers,
                HttpStatus.OK
        );

        when(bookingClient.confirmStatus(anyInt(), anyInt(), anyBoolean()))
                .thenReturn(expectedResult);

        mvc.perform(patch("/bookings/{bookingId}?approved={flag}", bookingId, approved)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void confirmStatuskk() throws Exception {
        int userId = 1;
        int bookingId = 1;
        boolean approved = false;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", Integer.toString(userId));
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "*/*");

        ResponseEntity<Object> expectedResult = new ResponseEntity<>(
                "response body from server",
                headers,
                HttpStatus.OK
        );

        when(bookingClient.confirmStatus(anyInt(), anyInt(), anyBoolean()))
                .thenReturn(expectedResult);

        mvc.perform(patch("/bookings/{bookingId}?approved={flag}", bookingId, approved)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getById() throws Exception {
        int userId = 1;
        int bookingId = 1;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", Integer.toString(userId));
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "*/*");

        ResponseEntity<Object> expectedResult = new ResponseEntity<>(
                "response body from server",
                headers,
                HttpStatus.OK
        );

        when(bookingClient.getById(userId, bookingId))
                .thenReturn(expectedResult);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllByUser() throws Exception {
        int userId = 1;
        String state = "ALL";
        Integer from = 0;
        Integer size = 10;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", Integer.toString(userId));
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "*/*");

        ResponseEntity<Object> expectedResult = new ResponseEntity<>(
                "response body from server",
                headers,
                HttpStatus.OK
        );

        when(bookingClient.getAllByUser(anyInt(), any(BookingState.class), anyInt(), anyInt()))
                .thenReturn(expectedResult);

        mvc.perform(get("/bookings?state={state}&from={from}&size={size}", state, from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAllByItemOwner() throws Exception {
        int userId = 1;
        String state = "ALL";
        Integer from = 0;
        Integer size = 10;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", Integer.toString(userId));
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "*/*");

        ResponseEntity<Object> expectedResult = new ResponseEntity<>(
                "response body from server",
                headers,
                HttpStatus.OK
        );

        when(bookingClient.getAllByItemOwner(anyInt(), any(BookingState.class), anyInt(), anyInt()))
                .thenReturn(expectedResult);

        mvc.perform(get("/bookings/owner?state={state}&from={from}&size={size}", state, from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON));
    }
}