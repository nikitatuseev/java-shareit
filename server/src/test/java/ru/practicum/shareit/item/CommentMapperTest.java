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
    public void toComment() {
        CreateCommentDto createCommentDto = new CreateCommentDto();
        createCommentDto.setText("text");

        Comment comment = commentMapper.toComment(createCommentDto);

        assertEquals(createCommentDto.getText(), comment.getText());
    }

    @Test
    public void toCommentDto() {
        Comment comment = new Comment();
        comment.setId(1);
        comment.setText("text");
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

