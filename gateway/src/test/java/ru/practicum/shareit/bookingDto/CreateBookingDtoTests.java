package ru.practicum.shareit.bookingDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CreateBookingDtoTests {

    @Autowired
    private ObjectMapper objectMapper;

    private CreateBookingDto createBookingDto;

    @BeforeEach
    public void setup() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now();
        int itemId = 1;

        createBookingDto = CreateBookingDto.builder()
                .start(start)
                .end(end)
                .itemId(itemId)
                .build();
    }

    @Test
    public void testSerialization() throws Exception {
        String jsonString = objectMapper.writeValueAsString(createBookingDto);
        assertThat(jsonString).isNotEmpty();
    }

    @Test
    public void testDeserialization() throws Exception {
        String jsonString = objectMapper.writeValueAsString(createBookingDto);
        CreateBookingDto deserializedBookingDto = objectMapper.readValue(jsonString, CreateBookingDto.class);
        assertThat(deserializedBookingDto).isEqualTo(createBookingDto);
    }
}

