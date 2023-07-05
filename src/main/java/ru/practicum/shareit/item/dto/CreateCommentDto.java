package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class CreateCommentDto {
    @NotNull(message = "текст не может быть пустым")
    @Size(min = 1, max = 256, message = "длина текста не может быть больше 256")
    private String text;
}
