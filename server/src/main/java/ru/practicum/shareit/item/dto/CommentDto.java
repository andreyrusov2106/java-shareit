package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Builder
@Value
public class CommentDto {
    Long id;
    String text;
    LocalDateTime created;
    String authorName;
}
