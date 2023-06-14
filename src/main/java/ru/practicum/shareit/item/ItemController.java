package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.CreateGroup;
import ru.practicum.shareit.UpdateGroup;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.groups.Default;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") int ownerId,
                          @RequestBody @Validated({Default.class, CreateGroup.class}) ItemDto itemDto) {
        return itemService.create(ownerId, itemDto);
    }

    @GetMapping("/{id}")
    public ItemDto getById(@RequestHeader("X-Sharer-User-Id") int userId,
                           @PathVariable int id) {
        return itemService.getById(userId, id);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") int ownerId,
                          @PathVariable int id,
                          @RequestBody @Validated({Default.class, UpdateGroup.class}) ItemDto itemDto) {
        return itemService.update(ownerId, id, itemDto);
    }

    @GetMapping
    public List<ItemDto> getAllByOwnerId(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.getAllByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> getAllByNameOrDescription(@RequestHeader("X-Sharer-User-Id") int userId,
                                           @RequestParam(name = "text") String substring) {
        return itemService.getAllByNameOrDescription(userId, substring);
    }
}
