package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Value
public class CommentDto {
    Long id;
    @NotNull
    @NotBlank
    String text;
    LocalDateTime created;
    String authorName;


}
