package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();

    }

    public static void toComment(Comment comment, CommentDto commentDto) {
        if (commentDto.getId() != null) comment.setId(commentDto.getId());
        if (commentDto.getText() != null) comment.setText(commentDto.getText());
        if (commentDto.getCreated() != null) comment.setCreated(commentDto.getCreated());
    }
}
