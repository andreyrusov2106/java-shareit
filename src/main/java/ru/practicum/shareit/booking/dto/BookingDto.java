package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.NonFinal;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Value
@ToString
public class BookingDto {
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
    User booker;
    @NonFinal
    Item item;
}
