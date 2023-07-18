package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ItemServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void create() {
        int ownerId = 1;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("text");

        Item item = new Item();
        item.setId(1);
        item.setName("item");
        item.setDescription("text");
        User owner = new User();
        owner.setId(ownerId);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(itemMapper.toItem(itemDto)).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto createdItemDto = itemService.create(ownerId, itemDto);

        assertEquals(itemDto.getName(), createdItemDto.getName());
        assertEquals(itemDto.getDescription(), createdItemDto.getDescription());
    }

    @Test
    public void getById() {
        int userId = 1;
        int itemId = 1;

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(itemId);
        item.setOwner(user);

        List<Comment> comments = new ArrayList<>();

        ItemDtoWithComments expectedItemDtoWithComments = new ItemDtoWithComments();
        expectedItemDtoWithComments.setId(itemId);
        expectedItemDtoWithComments.setOwnerId(userId);
        expectedItemDtoWithComments.setComments(new ArrayList<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findByItem(item)).thenReturn(comments);
        when(itemMapper.toItemDtoWithComments(item, comments)).thenReturn(expectedItemDtoWithComments);

        ItemDtoWithComments result = itemService.getById(userId, itemId);

        assertEquals(expectedItemDtoWithComments.getId(), result.getId());
        assertEquals(expectedItemDtoWithComments.getOwnerId(), result.getOwnerId());
        assertEquals(expectedItemDtoWithComments.getComments().size(), result.getComments().size());
    }

    @Test
    public void getAllByOwner() {

        int ownerId = 1;
        int from = 0;
        int size = 10;

        User owner = new User();
        owner.setId(ownerId);

        Item item1 = new Item();
        item1.setId(1);
        item1.setOwner(owner);

        Item item2 = new Item();
        item2.setId(2);
        item2.setOwner(owner);

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        List<Comment> comments1 = new ArrayList<>();
        List<Comment> comments2 = new ArrayList<>();

        ItemDtoWithComments itemDtoWithComments1 = new ItemDtoWithComments();
        itemDtoWithComments1.setId(1);
        itemDtoWithComments1.setOwnerId(ownerId);
        itemDtoWithComments1.setComments(new ArrayList<>());

        ItemDtoWithComments itemDtoWithComments2 = new ItemDtoWithComments();
        itemDtoWithComments2.setId(2);
        itemDtoWithComments2.setOwnerId(ownerId);
        itemDtoWithComments2.setComments(new ArrayList<>());

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(itemRepository.findAllByOwner(owner, PageRequest.of(0, 10))).thenReturn(items);
        when(commentRepository.findByItem(item1)).thenReturn(comments1);
        when(commentRepository.findByItem(item2)).thenReturn(comments2);
        when(itemMapper.toItemDtoWithComments(item1, comments1)).thenReturn(itemDtoWithComments1);
        when(itemMapper.toItemDtoWithComments(item2, comments2)).thenReturn(itemDtoWithComments2);
    }

    @Test
    public void update() {

        int ownerId = 1;
        int itemId = 1;
        User owner = new User();
        owner.setId(ownerId);

        Item item = new Item();
        item.setId(itemId);
        item.setOwner(owner);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("text");

        Item updatedItem = new Item();
        updatedItem.setId(itemId);
        updatedItem.setOwner(owner);
        updatedItem.setName("item");
        updatedItem.setDescription("text");

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(updatedItem);
        when(itemMapper.toItemDto(updatedItem)).thenReturn(itemDto);

        ItemDto updatedItemDto = itemService.update(ownerId, itemId, itemDto);

        assertEquals(itemDto.getName(), updatedItemDto.getName());
        assertEquals(itemDto.getDescription(), updatedItemDto.getDescription());
    }

    @Test
    public void getAllByNameOrDescription() {
        String text = "text";
        int from = 0;
        int size = 10;

        Item item1 = new Item();
        item1.setId(1);
        item1.setName("item1");
        item1.setDescription("text1");

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("item2");
        item2.setDescription("text2");

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        List<ItemDto> expectedItemDtos = new ArrayList<>();
        expectedItemDtos.add(itemMapper.toItemDto(item1));
        expectedItemDtos.add(itemMapper.toItemDto(item2));

        when(itemRepository.searchWithPaging(text.toLowerCase(), PageRequest.of(0, 10))).thenReturn(new PageImpl<>(items));
        when(itemMapper.toItemDto(item1)).thenReturn(expectedItemDtos.get(0));
        when(itemMapper.toItemDto(item2)).thenReturn(expectedItemDtos.get(1));

        List<ItemDto> result = itemService.getAllByNameOrDescription(text, from, size);

        assertEquals(expectedItemDtos.size(), result.size());
    }
}



