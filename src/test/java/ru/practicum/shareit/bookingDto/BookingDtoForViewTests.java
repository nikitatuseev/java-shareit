package ru.practicum.shareit.bookingDto;


import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoForView;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingDtoForViewTests {

    @Test
    public void testGetterAndSetterMethods() {
        int id = 1;
        int bookerId = 2;

        BookingDtoForView bookingDtoForView = new BookingDtoForView();
        bookingDtoForView.setId(id);
        bookingDtoForView.setBookerId(bookerId);

        assertEquals(id, bookingDtoForView.getId());
        assertEquals(bookerId, bookingDtoForView.getBookerId());
    }

}