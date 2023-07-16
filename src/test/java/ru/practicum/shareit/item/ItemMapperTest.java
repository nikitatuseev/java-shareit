package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ItemMapperTest {
    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private ItemMapper itemMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void toItemDtoWithValidItem() {
        Item item = new Item();
        item.setId(1);
        item.setOwner(new User());
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);

        ItemDto itemDto = itemMapper.toItemDto(item);

        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getOwner().getId(), itemDto.getOwnerId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
    }

    @Test
    public void toItemWithValidItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);

        Item item = itemMapper.toItem(itemDto);

        assertEquals(itemDto.getId(), item.getId());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getAvailable(), item.getAvailable());
    }

    @Test
    public void toItemDtoWithCommentsWithValidItemAndComments() {
        Item item = new Item();
        item.setId(1);
        item.setOwner(new User());
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);

        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment());
        comments.add(new Comment());

        when(commentMapper.toCommentDto(any(Comment.class))).thenReturn(new CommentDto());

        ItemDtoWithComments itemDtoWithComments = itemMapper.toItemDtoWithComments(item, comments);

        assertEquals(item.getId(), itemDtoWithComments.getId());
        assertEquals(item.getOwner().getId(), itemDtoWithComments.getOwnerId());
        assertEquals(item.getName(), itemDtoWithComments.getName());
        assertEquals(item.getDescription(), itemDtoWithComments.getDescription());
        assertEquals(item.getAvailable(), itemDtoWithComments.getAvailable());
        assertEquals(comments.size(), itemDtoWithComments.getComments().size());
    }

}
