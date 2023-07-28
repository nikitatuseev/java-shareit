package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") int ownerId,
                          @RequestBody ItemDto itemDto) {
        return itemService.create(ownerId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader("X-Sharer-User-Id") int userId,
                           @PathVariable int itemId) {
        return itemService.getById(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") int ownerId,
                          @PathVariable int itemId,
                          @RequestBody ItemDto itemDto) {
        return itemService.update(ownerId, itemId, itemDto);
    }

    @GetMapping
    public List<ItemDto> getAllByOwnerId(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemService.getAllByOwnerId(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> getAllBySubstring(@RequestHeader("X-Sharer-User-Id") int userId,
                                           @RequestParam(name = "text") String substring,
                                           @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemService.getAllByNameOrDescription(substring, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                    @PathVariable int itemId,
                                    @RequestBody CreateCommentDto creationCommentDto) {
        return itemService.createComment(userId, itemId, creationCommentDto);
    }
}