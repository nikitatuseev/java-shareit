package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;

import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;


import java.time.LocalDateTime;
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
    void getById() {
        int userId = 999;
        int id = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getById(userId, id));
        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getByIdItemNotFound() {
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
    void updad() {
        int ownerId = 1;
        int id = 1;
        ItemDto itemDto = new ItemDto();
        User owner = new User(1, "name", "e@mail.com");

        Item existingItem = new Item();
        existingItem.setOwner(new User(2, "name1", "n@mail.com"));

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(id)).thenReturn(Optional.of(existingItem));
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
    void createComment() {
        int userId = 1;
        int itemId = 1;
        User user = new User();
        Item item = new Item();
        Comment comment = new Comment();
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByBookerAndItemAndStatusAndEndBefore(
                any(User.class),
                any(Item.class),
                any(BookingStatus.class),
                any(LocalDateTime.class)))
                .thenReturn(Optional.of(new Booking()));
        when(commentMapper.toComment(any(CreateCommentDto.class)))
                .thenReturn(comment);
        when(commentRepository.save(comment))
                .thenReturn(comment);
        when(commentMapper.toCommentDto(comment))
                .thenReturn(new CommentDto());

        CommentDto commentDto = itemService.createComment(userId, itemId, new CreateCommentDto());

        assertNotNull(commentDto);
        verify(commentMapper).toCommentDto(commentArgumentCaptor.capture());
        Comment resultComment = commentArgumentCaptor.getValue();
        assertEquals(comment, resultComment);
        assertEquals(user, resultComment.getUser());
        assertEquals(item, resultComment.getItem());
        assertTrue(resultComment.getCreated().isBefore(LocalDateTime.now()));
        verify(userRepository, times(1))
                .findById(anyInt());
        verify(itemRepository, times(1))
                .findById(anyInt());
        verify(bookingRepository, times(1))
                .findFirstByBookerAndItemAndStatusAndEndBefore(
                        any(User.class),
                        any(Item.class),
                        any(BookingStatus.class),
                        any(LocalDateTime.class));
        verify(commentMapper, times(1)).toComment(any(CreateCommentDto.class));
        verify(commentRepository, times(1)).save(comment);
        verify(commentMapper, times(1)).toCommentDto(any(Comment.class));
        verifyNoMoreInteractions(userRepository,
                itemRepository,
                bookingRepository,
                commentMapper,
                commentRepository
        );
    }


    @Test
    void updateUserNotFound() {
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
    void createCommentBookingNotFound() {
        int userId = 1;
        int itemId = 1;
        User user = new User();
        Item item = new Item();
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByBookerAndItemAndStatusAndEndBefore(
                any(User.class),
                any(Item.class),
                any(BookingStatus.class),
                any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        assertThrows(ValidationException.class,
                () -> itemService.createComment(userId, itemId, new CreateCommentDto()));

        verify(userRepository, times(1))
                .findById(anyInt());
        verify(itemRepository, times(1))
                .findById(anyInt());
        verify(bookingRepository, times(1))
                .findFirstByBookerAndItemAndStatusAndEndBefore(
                        any(User.class),
                        any(Item.class),
                        any(BookingStatus.class),
                        any(LocalDateTime.class));
        verifyNoMoreInteractions(userRepository, itemRepository, bookingRepository);
    }

    @Test
    void createCommentItemNotFound() {
        int userId = 1;
        int itemId = 999;

        User user = new User();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.createComment(userId, itemId, new CreateCommentDto()));

        verify(userRepository, times(1))
                .findById(anyInt());
        verify(itemRepository, times(1))
                .findById(anyInt());
        verifyNoMoreInteractions(userRepository, itemRepository);
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

    @Test
    void userFoundAndItemRequestFound() {
        int ownerId = 1;
        int requestId = 1;
        ItemDto itemDto = new ItemDto();
        itemDto.setRequestId(requestId);
        itemDto.setOwnerId(ownerId);
        itemDto.setName("item");
        Item item = new Item();
        item.setName("item");
        User user = new User();
        user.setName("name");

        ItemRequest request = new ItemRequest();
        request.setId(requestId);

        when(itemMapper.toItem(itemDto)).thenReturn(item);
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));
        when(requestRepository.findById(itemDto.getRequestId())).thenReturn(Optional.of(request));
        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto resultItemDto = itemService.create(ownerId, itemDto);

        assertEquals(itemDto, resultItemDto);
        assertEquals(resultItemDto.getOwnerId(), ownerId);
        assertEquals(resultItemDto.getRequestId(), requestId);
        verify(itemMapper, times(1)).toItem(itemDto);
        verify(userRepository, times(1)).findById(ownerId);
        verify(requestRepository, times(1)).findById(requestId);
        verify(itemRepository, times(1)).save(item);
        verifyNoMoreInteractions(itemMapper, userRepository, requestRepository, itemRepository);
    }
}