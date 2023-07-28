package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping
    ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") int userId,
                      @RequestBody NewRequestDto newRequestDto) {
        return requestService.create(userId, newRequestDto);
    }

    @GetMapping("/{requestId}")
    ItemRequestDto getById(@RequestHeader("X-Sharer-User-Id") int userId,
                       @PathVariable int requestId) {
        return requestService.getById(userId, requestId);
    }

    @GetMapping
    List<ItemRequestDto> getAllByUser(@RequestHeader("X-Sharer-User-Id") int userId) {
        return requestService.getAllByUser(userId);
    }

    @GetMapping("/all")
    List<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") int userId,
                            @RequestParam(name = "from", defaultValue = "0") Integer from,
                            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return requestService.getAll(userId, from, size);
    }
}