package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.NonFinal;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Value
public class ItemDto {
    Long id;
    @NotNull
    @NotBlank
    String name;
    @NotNull
    @NotBlank
    String description;
    @NotNull
    Boolean available;
    @NonFinal
    @Setter
    BookingDto lastBooking;
    @NonFinal
    @Setter
    BookingDto nextBooking;
    @NonFinal
    @Setter
    List<CommentDto> comments;
    @NonFinal
    @Setter
    Long requestId;
}
