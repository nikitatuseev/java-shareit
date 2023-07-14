package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.util.List;

public interface BookingService {
    BookingDto create(int userId, CreateBookingDto newBookingDto);

    BookingDto confirmStatus(int ownerId, int bookingId, boolean approved);

    BookingDto getById(int userId, int bookingId);

    List<BookingDto> getAllByUser(int userId, String state);

    List<BookingDto> getAllByItemOwner(int userId, String state);
}