package ru.practicum.shareit.user.dto;

import lombok.*;
import lombok.experimental.NonFinal;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Value
public class UserDto {
    @NonFinal
    Long id;
    @NonFinal
    String name;
    @NonFinal
    String email;


}
