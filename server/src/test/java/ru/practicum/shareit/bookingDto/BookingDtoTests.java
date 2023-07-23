package ru.practicum.shareit.bookingDto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingDtoTests {

    @Test
    public void testGetterAndSetterMethods() {
        int id = 1;
        ItemDto item = new ItemDto();
        UserDto booker = new UserDto();
        BookingStatus status = BookingStatus.WAITING;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now();

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(id);
        bookingDto.setItem(item);
        bookingDto.setBooker(booker);
        bookingDto.setStatus(status);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);

        assertEquals(id, bookingDto.getId());
        assertEquals(item, bookingDto.getItem());
        assertEquals(booker, bookingDto.getBooker());
        assertEquals(status, bookingDto.getStatus());
        assertEquals(start, bookingDto.getStart());
        assertEquals(end, bookingDto.getEnd());
    }
}
