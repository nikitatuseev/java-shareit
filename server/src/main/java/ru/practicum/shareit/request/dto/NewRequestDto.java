package ru.practicum.shareit.request.dto;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewRequestDto {
    private String description;
}