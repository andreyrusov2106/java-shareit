package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.NonFinal;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Value
public class ItemDto {
    @NonFinal
    Long id;
    @NonFinal
    String name;
    @NonFinal
    String description;
    @NonFinal
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
