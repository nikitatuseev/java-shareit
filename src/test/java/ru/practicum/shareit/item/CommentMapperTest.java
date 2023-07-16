package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentMapperTest {
    private CommentMapper commentMapper;

    @BeforeEach
    public void setUp() {
        commentMapper = new CommentMapper();
    }

    @Test
    public void toCommentWithValidCreateCommentDto() {
        CreateCommentDto createCommentDto = new CreateCommentDto();
        createCommentDto.setText("Test Comment");

        Comment comment = commentMapper.toComment(createCommentDto);

        assertEquals(createCommentDto.getText(), comment.getText());
    }

    @Test
    public void toCommentDtoWithValidComment() {
        Comment comment = new Comment();
        comment.setId(1);
        comment.setText("Test Comment");
        User user = new User();
        user.setName("Name name");
        comment.setUser(user);
        comment.setCreated(LocalDateTime.now());

        CommentDto commentDto = commentMapper.toCommentDto(comment);

        assertEquals(comment.getId(), commentDto.getId());
        assertEquals(comment.getText(), commentDto.getText());
        assertEquals(comment.getUser().getName(), commentDto.getAuthorName());
        assertEquals(comment.getCreated(), commentDto.getCreated());
    }
}

