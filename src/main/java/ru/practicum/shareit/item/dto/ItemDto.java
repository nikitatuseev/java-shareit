package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.CreateGroup;
import ru.practicum.shareit.booking.dto.BookingDtoForView;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class ItemDto {
    private int id;
    private int ownerId;

    @NotNull(groups = CreateGroup.class, message = "имя не может быть пустым")
    @Size(min = 1, max = 256, message = "имя не может быть больше 256")
    private String name;

    @NotNull(groups = CreateGroup.class, message = "описание не может быть пустым")
    @Size(min = 1, max = 256, message = "описание не может быть длинее 256")
    private String description;

    @NotNull(groups = CreateGroup.class, message = "доступность не может быть пустой")
    private Boolean available;
    private BookingDtoForView lastBooking;
    private BookingDtoForView nextBooking;
}