package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@Data
@NoArgsConstructor
public class BookingDto {
    private int id;
    private ItemDto item;
    private UserDto booker;
    private String status;
    private String start;
    private String end;
}