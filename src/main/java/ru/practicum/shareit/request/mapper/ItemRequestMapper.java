package ru.practicum.shareit.request.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestMapper {
    private final ItemMapper itemMapper;

    public ItemRequest toRequest(NewRequestDto newRequestDto, User requestor) {
        ItemRequest request = new ItemRequest();

        request.setDescription(newRequestDto.getDescription());
        request.setRequestor(requestor);

        return request;
    }

    public ItemRequestDto toRequestDto(ItemRequest request) {
        ItemRequestDto requestDto = new ItemRequestDto();

        requestDto.setId(request.getId());
        requestDto.setDescription(request.getDescription());
        requestDto.setCreated(request.getCreated());
        requestDto.setRequestorId(request.getRequestor().getId());
        requestDto.setItems(request.getItems().stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList()));

        return requestDto;
    }
}