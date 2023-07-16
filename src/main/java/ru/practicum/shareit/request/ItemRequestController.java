package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping
    ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") int userId,
                      @RequestBody @Valid NewRequestDto newRequestDto) {
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
                            @RequestParam(defaultValue = "0", required = false) @Min(0) int from,
                            @RequestParam(defaultValue = "10", required = false) @Min(1) int size) {
        return requestService.getAll(userId, from, size);
    }
}