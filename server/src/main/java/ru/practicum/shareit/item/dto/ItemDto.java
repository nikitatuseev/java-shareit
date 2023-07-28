package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoForView;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private int id;
    private int ownerId;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoForView lastBooking;
    private BookingDtoForView nextBooking;
    private Integer requestId;
}