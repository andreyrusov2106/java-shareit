package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.NonFinal;
import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoCreate;


import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Value
@ToString
public class BookingResponseDto {
    @NonFinal
    Long id;
    @NonFinal
    LocalDateTime start;
    @NonFinal
    LocalDateTime end;
    @NonFinal
    Status status;
    @NonFinal
    Long itemId;
    @NonFinal
    Long bookerId;
    @NonFinal
    UserDtoCreate booker;
    @NonFinal
    ItemDtoCreate item;
}
