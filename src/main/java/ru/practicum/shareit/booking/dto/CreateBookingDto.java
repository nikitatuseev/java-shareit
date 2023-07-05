package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.practicum.shareit.CreateGroup;
import ru.practicum.shareit.UpdateGroup;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateBookingDto {
    @NotNull(groups = {CreateGroup.class, UpdateGroup.class},message = "Дата начала бронирования не может быть пустой")
    @FutureOrPresent(groups = {CreateGroup.class, UpdateGroup.class},message = "Дата начала бронирования не может быть в прошлом")
    private LocalDateTime start;

    @NotNull(groups = {CreateGroup.class, UpdateGroup.class},message = "Дата окончания бронирования не может быть пустой")
    @FutureOrPresent(groups = {CreateGroup.class, UpdateGroup.class},message = "Дата окончания бронирования не может быть в прошлом")
    private LocalDateTime end;

    @NotNull(groups = CreateGroup.class, message = "Не указана вещь")
    private int itemId;
}

