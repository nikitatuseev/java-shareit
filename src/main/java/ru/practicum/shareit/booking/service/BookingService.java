package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreatBookingDto;

import java.util.List;

public interface BookingService {
    BookingDto create(int userId, CreatBookingDto creatBookingDto);

    BookingDto confirmStatus(int ownerId, int bookingId, String approved);

    BookingDto getById(int userId, int bookingId);

    List<BookingDto> getAllByUser(int userId, String state);

    List<BookingDto> getAllByItemOwner(int userId, String state);
}