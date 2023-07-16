package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.CreateGroup;
import ru.practicum.shareit.UpdateGroup;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.service.ItemService;


import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") int ownerId,
                          @RequestBody @Validated({CreateGroup.class}) ItemDto itemDto) {
        return itemService.create(ownerId, itemDto);
    }

    @GetMapping("/{id}")
    public ItemDtoWithComments getById(@RequestHeader("X-Sharer-User-Id") int userId,
                                       @PathVariable @Min(1) int id) {
        return (ItemDtoWithComments) itemService.getById(userId, id);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") int ownerId,
                          @PathVariable @Min(1) int id,
                          @RequestBody @Validated({UpdateGroup.class}) ItemDto itemDto) {
        return itemService.update(ownerId, id, itemDto);
    }

    @GetMapping
    public List<ItemDto> getAllByOwnerId(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                         @RequestParam(defaultValue = "0", required = false) @Min(0) int from,
                                         @RequestParam(defaultValue = "10", required = false) @Min(1) int size) {
        return itemService.getAllByOwnerId(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> getAllBySubstring(@RequestHeader("X-Sharer-User-Id") int userId,
                                           @RequestParam(name = "text") String substring,
                                           @RequestParam(defaultValue = "0", required = false) @Min(0) int from,
                                           @RequestParam(defaultValue = "10", required = false) @Min(1) int size) {
        return itemService.getAllByNameOrDescription(substring, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                    @PathVariable @Min(1) int itemId,
                                    @RequestBody @Valid CreateCommentDto createCommentDto) {
        return itemService.createComment(userId, itemId, createCommentDto);
    }
}
