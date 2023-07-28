package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class CreateBookingDto {
    @NotNull(message = "Дата начала бронирования не может быть пустой")
    @FutureOrPresent(message = "Дата начала бронирования не может быть в прошлом")
    private LocalDateTime start;

    @NotNull(message = "Дата окончания бронирования не может быть пустой")
    @FutureOrPresent(message = "Дата окончания бронирования не может быть в прошлом")
    private LocalDateTime end;

    @NotNull(message = "Не указана вещь")
    private int itemId;
}

