package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreatCommentDto;
import ru.practicum.shareit.item.model.Comment;

@Service
public class CommentMapper {
    public Comment toComment(CreatCommentDto creatCommentDto) {
        Comment comment = new Comment();
        comment.setText(creatCommentDto.getText());
        return comment;
    }

    public CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getUser().getName());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }
}