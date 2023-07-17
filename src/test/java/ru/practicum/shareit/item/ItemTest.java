package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;

import ru.practicum.shareit.exeption.NotFoundException;

import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private ItemMapper itemMapper;
    @InjectMocks
    private ItemServiceImpl itemService;
    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;
    @Captor
    private ArgumentCaptor<Comment> commentArgumentCaptor;

    @Test
    void create() {
        int ownerId = 1;

        ItemDto itemDto = new ItemDto();
        itemDto.setOwnerId(ownerId);
        itemDto.setName("name");

        Item item = new Item();
        User user = new User();
        user.setName("item");

        when(itemMapper.toItem(itemDto)).thenReturn(item);
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));
        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto resultItemDto = itemService.create(ownerId, itemDto);

        assertEquals(itemDto, resultItemDto);
        assertEquals(itemDto.getOwnerId(), ownerId);
        assertNull(itemDto.getRequestId());
        verify(itemMapper, times(1)).toItem(itemDto);
        verify(userRepository, times(1)).findById(ownerId);
        verify(itemRepository, times(1)).save(item);
        verify(itemMapper, times(1)).toItemDto(item);
        verifyNoMoreInteractions(itemMapper, userRepository, itemRepository);
    }

    @Test
    void UserNotFound() {
        int userId = 999;
        int id = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.getById(userId, id));
        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void ItemNotFound() {
        int userId = 1;
        int id = 999;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getById(userId, id));
        verify(userRepository, times(1)).findById(userId);
        verify(itemRepository, times(1)).findById(id);
        verifyNoMoreInteractions(userRepository, itemRepository);
    }

    @Test
    void update() {
        int ownerId = 1;
        int id = 1;

        ItemDto itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("text");
        itemDto.setAvailable(true);

        User owner = new User(1, "name", "e@mail.com");

        Item existingItem = new Item();
        existingItem.setOwner(owner);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(id)).thenReturn(Optional.of(existingItem));
        when(itemRepository.save(any(Item.class))).thenReturn(existingItem);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(new ItemDto());

        ItemDto updatedItemDto = itemService.update(ownerId, id, itemDto);

        assertNotNull(updatedItemDto);

        verify(itemMapper).toItemDto(itemArgumentCaptor.capture());
        Item savedItem = itemArgumentCaptor.getValue();

        assertNotNull(savedItem);
        assertEquals(itemDto.getName(), savedItem.getName());
        assertEquals(itemDto.getDescription(), savedItem.getDescription());
        assertEquals(itemDto.getAvailable(), savedItem.getAvailable());
        verify(userRepository, times(1)).findById(anyInt());
        verify(itemRepository, times(1)).findById(id);
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(itemMapper, times(1)).toItemDto(any(Item.class));
        verifyNoMoreInteractions(userRepository, itemRepository, itemMapper);
    }

    @Test
    void updateUserNotFound_() {
        int ownerId = 999;
        int id = 1;
        ItemDto itemDto = new ItemDto();
        when(userRepository.findById(ownerId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.update(ownerId, id, itemDto));
        verify(userRepository, times(1)).findById(anyInt());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateItemNotFound() {
        int ownerId = 1;
        int id = 999;
        ItemDto itemDto = new ItemDto();
        User user = new User();
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.update(ownerId, id, itemDto));
        verify(userRepository, times(1)).findById(anyInt());
        verify(itemRepository, times(1)).findById(anyInt());
        verifyNoMoreInteractions(userRepository, itemRepository);
    }

    @Test
    void getAllByOwnerId() {
        int ownerId = 1;

        when(userRepository.findById(ownerId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getAllByOwnerId(ownerId, 0, 10));
        verify(userRepository, times(1))
                .findById(anyInt());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void createCommentUserNotFound() {
        int userId = 999;
        int itemId = 1;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.createComment(userId, itemId, new CreateCommentDto()));

        verify(userRepository, times(1))
                .findById(anyInt());
        verifyNoMoreInteractions(userRepository);
    }
}