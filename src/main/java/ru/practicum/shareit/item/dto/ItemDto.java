package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.CreateGroup;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private int id;

    @NotBlank(groups = CreateGroup.class, message = "Название не может быть пустым")
    @Size(min = 1, max = 64)
    private String name;

    @NotNull(groups = CreateGroup.class, message = "Описание не может быть пустым")
    @Size(min = 1, max = 256)
    private String description;

    @NotNull(groups = CreateGroup.class)
    private Boolean available;
    private User owner;
}