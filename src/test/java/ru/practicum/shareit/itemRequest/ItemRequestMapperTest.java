package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ItemRequestMapperTest {
    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemRequestMapper itemRequestMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void toRequestWithNewRequestDtoAndRequestor() {
        NewRequestDto newRequestDto = new NewRequestDto();
        newRequestDto.setDescription("Test Request Description");

        User requestor = new User();

        ItemRequest result = itemRequestMapper.toRequest(newRequestDto, requestor);

        assertEquals(newRequestDto.getDescription(), result.getDescription());
        assertEquals(requestor, result.getRequestor());
    }

    @Test
    public void toRequestDtoWithItemRequest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setDescription("Test Request Description");
        itemRequest.setCreated(LocalDateTime.now());

        User requestor = new User();
        requestor.setId(1);

        List<Item> items = new ArrayList<>();
        Item item1 = new Item();
        item1.setId(1);
        items.add(item1);
        Item item2 = new Item();
        item2.setId(2);
        items.add(item2);

        List<ItemDto> itemDtos = new ArrayList<>();
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setId(1);
        itemDtos.add(itemDto1);
        ItemDto itemDto2 = new ItemDto();
        itemDto2.setId(2);
        itemDtos.add(itemDto2);

        itemRequest.setRequestor(requestor);
        itemRequest.setItems(items);

        when(itemMapper.toItemDto(item1)).thenReturn(itemDto1);
        when(itemMapper.toItemDto(item2)).thenReturn(itemDto2);

        ItemRequestDto result = itemRequestMapper.toRequestDto(itemRequest);

        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(itemRequest.getDescription(), result.getDescription());
        assertEquals(itemRequest.getCreated().toString(), result.getCreated());
        assertEquals(requestor.getId(), result.getRequestorId());
        assertEquals(itemDtos.size(), result.getItems().size());
    }
}
