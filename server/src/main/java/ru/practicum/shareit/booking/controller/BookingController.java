package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") int userId,
                             @RequestBody CreateBookingDto newBookingDto) {
        return bookingService.create(userId, newBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto confirmStatus(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                    @PathVariable int bookingId,
                                    @RequestParam(name = "approved") boolean approved) {
        return bookingService.confirmStatus(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllByUser(@RequestHeader("X-Sharer-User-Id") int userId,
                                         @RequestParam(name = "state", defaultValue = "ALL") String state,
                                         @RequestParam(defaultValue = "0", required = false) int from,
                                         @RequestParam(defaultValue = "10", required = false) int size) {
        return bookingService.getAllByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByItemOwner(@RequestHeader("X-Sharer-User-Id") int userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String state,
                                              @RequestParam(defaultValue = "0", required = false) int from,
                                              @RequestParam(defaultValue = "10", required = false) int size) {
        return bookingService.getAllByItemOwner(userId, state, from, size);
    }
}