package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exeption.ValidationException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

//я не понимаю где ошибка в booking потому что я же не менял логику и раньше все работало
//а сейчас неправильно работает @PatchMapping("/{bookingId}") из-за чего остальные тесты тоже ломаются
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") int userId,
                                         @RequestBody @Validated CreateBookingDto createBookingDto) {
        return bookingClient.create(userId, createBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> confirmStatus(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                                @PathVariable int bookingId,
                                                @RequestParam(name = "approved") boolean approved) {

        return bookingClient.confirmStatus(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") int userId,
                                          @PathVariable int bookingId) {
        return bookingClient.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUser(@RequestHeader("X-Sharer-User-Id") int userId,
                                               @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                               @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new ValidationException("Unknown state: " + stateParam));

        return bookingClient.getAllByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByItemOwner(@RequestHeader("X-Sharer-User-Id") int userId,
                                                    @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                    @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                    @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new ValidationException("Unknown state: " + stateParam));

        return bookingClient.getAllByItemOwner(userId, state, from, size);
    }
}
