package ru.practicum.shareit.request.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewRequestDto {
    @NotNull(message = "описание не может быть пустым")
    @Size(min = 1, max = 256, message = "описание не должно быть длиннее 256")
    private String description;
}