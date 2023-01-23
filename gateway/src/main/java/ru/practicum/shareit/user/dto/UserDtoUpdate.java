package ru.practicum.shareit.user.dto;

import lombok.*;
import lombok.experimental.NonFinal;

import javax.validation.constraints.Email;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Value
public class UserDtoUpdate {
    @NonFinal
    Long id;
    @NonFinal
    String name;
    @Email
    @NonFinal
    String email;


}
