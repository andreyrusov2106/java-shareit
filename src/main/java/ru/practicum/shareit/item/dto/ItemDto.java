package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.NonFinal;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotNull
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
