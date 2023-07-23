package ru.practicum.shareit.bookingDto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateBookingDtoTests {

    @Test
    public void testGetterAndSetterMethods() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now();
        int itemId = 1;

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .start(start)
                .end(end)
                .itemId(itemId)
                .build();

        assertEquals(start, createBookingDto.getStart());
        assertEquals(end, createBookingDto.getEnd());
        assertEquals(itemId, createBookingDto.getItemId());
    }
}