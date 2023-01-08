package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.NonFinal;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Value
public class ItemDtoCreate {
    @NonFinal
    Long id;
    @NotBlank
    @NonFinal
    String name;
    @NotNull
    @NotBlank
    @NonFinal
    String description;
    @NotNull
    @NonFinal
    Boolean available;
    @NonFinal
    BookingResponseDto lastBooking;
    @NonFinal
    BookingResponseDto nextBooking;
    @NonFinal
    List<CommentDto> comments;
    @NonFinal
    Long requestId;
}
