package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.CreateGroup;
import ru.practicum.shareit.UpdateGroup;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                         @RequestBody @Validated(CreateGroup.class) ItemDto itemDto) {
        return itemClient.create(ownerId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") int userId,
                                          @PathVariable int itemId) {
        return itemClient.getById(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                         @PathVariable int itemId,
                                         @RequestBody @Validated({UpdateGroup.class}) ItemDto itemDto) {
        return itemClient.update(ownerId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByOwnerId(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                                  @RequestParam(name = "from", defaultValue = "0") @Positive int from,
                                                  @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        return itemClient.getAllByOwnerId(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getAllByNameOrDescription(@RequestHeader("X-Sharer-User-Id") int userId,
                                                    @RequestParam(name = "text") String text,
                                                    @RequestParam(name = "from", defaultValue = "0") @Positive int from,
                                                    @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        return itemClient.getAllByNameOrDescription(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @PathVariable int itemId,
                                                @RequestBody @Valid CreateCommentDto createCommentDto) {
        return itemClient.createComment(userId, itemId, createCommentDto);
    }
}