package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private int id;
    @NotBlank
    private LocalDateTime start;
    @NotBlank
    private LocalDateTime end;
    @NotBlank
    private Item item;
    @NotBlank
    private User booker;
    @NotBlank
    private String status;
}
