package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.CreateGroup;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;

    @NotNull(groups = CreateGroup.class, message = "Имя не может быть пустым")
    @Size(min = 1, max = 64)
    private String name;

    @NotNull(groups = CreateGroup.class, message = "Email не может быть пустым")
    @Email(message = "неправильный email")
    private String email;
}