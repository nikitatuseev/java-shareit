package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ItemDtoWithComments extends ItemDto {
    private List<CommentDto> comments;
}