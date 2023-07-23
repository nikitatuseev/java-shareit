package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
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

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestMapper itemRequestMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void toRequest() {
        NewRequestDto newRequestDto = new NewRequestDto();
        newRequestDto.setDescription("text");

        User requestor = new User();

        ItemRequest result = itemRequestMapper.toRequest(newRequestDto, requestor);

        assertEquals(newRequestDto.getDescription(), result.getDescription());
        assertEquals(requestor, result.getRequestor());
    }

    @Test
    public void toRequestDtoWithItemRequest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setDescription("text");
        itemRequest.setCreated(LocalDateTime.now());

        User requestor = new User();
        requestor.setId(1);
        itemRequest.setRequestor(requestor);

        List<Item> items = new ArrayList<>();
        Item item1 = new Item();
        item1.setId(1);
        items.add(item1);
        Item item2 = new Item();
        item2.setId(2);
        items.add(item2);

        when(itemMapper.toItemDto(item1)).thenReturn(new ItemDto());
        when(itemMapper.toItemDto(item2)).thenReturn(new ItemDto());
        when(itemRepository.findAllByRequest(itemRequest)).thenReturn(items);

        ItemRequestDto result = itemRequestMapper.toRequestDto(itemRequest);

        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(itemRequest.getDescription(), result.getDescription());
        assertEquals(requestor.getId(), result.getRequestorId());
        assertEquals(2, result.getItems().size());
    }

}
