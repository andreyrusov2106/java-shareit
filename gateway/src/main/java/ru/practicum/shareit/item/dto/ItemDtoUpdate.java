package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.NonFinal;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Value
public class ItemDtoUpdate {
    @NonFinal
    Long id;
    @NonFinal
    String name;
    @NonFinal
    String description;
    @NonFinal
    Boolean available;
    @NonFinal
    Long requestId;
}
