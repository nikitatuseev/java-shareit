package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.CreateGroup;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;

    @NotBlank(groups = CreateGroup.class, message = "Имя не может быть пустым")
    @Size(min = 1, max = 64)
    private String name;

    @NotEmpty(groups = CreateGroup.class, message = "Email не может быть пустым")
    @Email(groups = CreateGroup.class, message = "Введен некорректный email")
    private String email;
}