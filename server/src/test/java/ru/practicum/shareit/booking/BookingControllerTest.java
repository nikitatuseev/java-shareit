package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;

    @Test
    void create() throws Exception {
        int userId = 1;

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .itemId(1)
                .start((LocalDateTime.now().plusHours(1)))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        BookingDto expectedResult = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(2))
                .item(null)
                .booker(null)
                .status(BookingStatus.WAITING)
                .build();

        when(bookingService.create(anyInt(), any(CreateBookingDto.class))).thenReturn(expectedResult);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(createBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedResult.getId()), Integer.class))
                .andExpect(jsonPath("$.item", is(nullValue(ItemDto.class))))
                .andExpect(jsonPath("$.booker", is(nullValue(UserDto.class))));
    }

    @Test
    void createBookingDto() throws Exception {
        int userId = 1;

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .itemId(-1)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        verify(bookingService, never()).create(anyInt(), any(CreateBookingDto.class));
    }

    @Test
    void creationBookingDtoItemIdNull() throws Exception {
        int userId = 1;

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        verify(bookingService, never()).create(anyInt(), any(CreateBookingDto.class));
    }

    @Test
    void creationBookingDtoStartNull() throws Exception {
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
                        .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    void creationBookingDtoEndNull() throws Exception {
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
                        .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    void creationBookingDtoEndBeforeNow() throws Exception {
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
                        .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    void creationBookingDtoEndBeforeStart() throws Exception {
        int userId = 1;

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusMinutes(30))
                .build();

        verify(bookingService, never()).create(anyInt(), any(CreateBookingDto.class));
    }

    @Test
    void confirmStatusBookingIdValid() throws Exception {
        int userId = 1;
        int bookingId = 1;

        BookingDto expectedResult = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(2))
                .item(null)
                .booker(null)
                .status(BookingStatus.WAITING)
                .build();

        when(bookingService.confirmStatus(anyInt(), anyInt(), anyBoolean())).thenReturn(expectedResult);

        mvc.perform(patch("/bookings/{id}?approved=true", bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedResult.getId()), Integer.class))
                .andExpect(jsonPath("$.item", is(nullValue(ItemDto.class))))
                .andExpect(jsonPath("$.booker", is(nullValue(UserDto.class))));
    }

    @Test
    void confirmStatusBookingId() throws Exception {
        int userId = 1;
        int bookingId = -1;

        mvc.perform(patch("/bookings/{id}?approved=true", bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void creationBookingDtoEndEqualsStart() throws Exception {
        int userId = 1;

        LocalDateTime start = LocalDateTime.now().plusDays(1);

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .itemId(1)
                .start(start)
                .end(start)
                .build();

        verify(bookingService, never()).create(anyInt(), any(CreateBookingDto.class));
    }

    @Test
    void creationBookingDtoStartBeforeNow() throws Exception {
        int userId = 1;

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().minusMinutes(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(createBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    void getByIdBookingIdValid() throws Exception {
        int userId = 1;
        int bookingId = 1;

        BookingDto expectedResult = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(2))
                .item(null)
                .booker(null)
                .status(BookingStatus.WAITING)
                .build();

        when(bookingService.getById(anyInt(), anyInt())).thenReturn(expectedResult);

        mvc.perform(get("/bookings/{id}", bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedResult.getId()), Integer.class))
                .andExpect(jsonPath("$.item", is(nullValue(ItemDto.class))))
                .andExpect(jsonPath("$.booker", is(nullValue(UserDto.class))));
    }

    @Test
    void getByIdBookingId() throws Exception {
        int bookingId = -1;
        int userId = -1;

        mvc.perform(get("/bookings/{id}", bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    void getAllByUser() throws Exception {
        int userId = 1;

        BookingDto expectedResult = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(2))
                .item(null)
                .booker(null)
                .status(BookingStatus.WAITING)
                .build();

        when(bookingService.getAllByUser(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(expectedResult));

        mvc.perform(get("/bookings?from=0&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(expectedResult.getId()), Integer.class))
                .andExpect(jsonPath("$[0].item", is(nullValue(ItemDto.class))))
                .andExpect(jsonPath("$[0].booker", is(nullValue(UserDto.class))));
    }

    @Test
    void getAllByUserFromOrSizeNotValid() throws Exception {
        int userId = 1;

        mvc.perform(get("/bookings?from=-1&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        mvc.perform(get("/bookings?from=0&size=0")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        mvc.perform(get("/bookings?from=1&size=0")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        mvc.perform(get("/bookings?from=0&size=-1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(bookingService, never()).getAllByItemOwner(anyInt(), anyString(), anyInt(), anyInt());
    }

    @Test
    void getAllByItemOwnerFromAndSizeValid() throws Exception {
        int userId = 1;

        BookingDto expectedResult = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(2))
                .item(null)
                .booker(null)
                .status(BookingStatus.WAITING)
                .build();

        when(bookingService.getAllByItemOwner(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(expectedResult));

        mvc.perform(get("/bookings/owner?from=0&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(expectedResult.getId()), Integer.class))
                .andExpect(jsonPath("$[0].item", is(nullValue(ItemDto.class))))
                .andExpect(jsonPath("$[0].booker", is(nullValue(UserDto.class))));

        verify(bookingService, never()).getAllByUser(anyInt(), anyString(), anyInt(), anyInt());
    }

    @Test
    void getAllByItemOwner() throws Exception {
        int userId = 1;

        mvc.perform(get("/bookings/owner?from=0&size=0")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        mvc.perform(get("/bookings/owner?from=1&size=0")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        mvc.perform(get("/bookings/owner?from=0&size=-1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
}

