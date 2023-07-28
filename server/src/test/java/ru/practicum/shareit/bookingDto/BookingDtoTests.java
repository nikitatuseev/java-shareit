package ru.practicum.shareit.bookingDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoTests {

    @Autowired
    private ObjectMapper objectMapper;

    private BookingDto bookingDto;

    @BeforeEach
    public void setup() {
        int id = 1;
        ItemDto item = new ItemDto();
        UserDto booker = new UserDto();
        BookingStatus status = BookingStatus.WAITING;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now();

        bookingDto = BookingDto.builder()
                .id(id)
                .item(item)
                .booker(booker)
                .status(status)
                .start(start)
                .end(end)
                .build();
    }

    @Test
    public void testSerialization() throws Exception {
        String jsonString = objectMapper.writeValueAsString(bookingDto);
        assertThat(jsonString).isNotEmpty();
    }

    @Test
    public void testDeserialization() throws Exception {
        String jsonString = objectMapper.writeValueAsString(bookingDto);
        BookingDto deserializedBookingDto = objectMapper.readValue(jsonString, BookingDto.class);
        assertThat(deserializedBookingDto).isEqualTo(bookingDto);
    }
}

