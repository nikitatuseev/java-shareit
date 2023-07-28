package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookingDto {
    private int itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}