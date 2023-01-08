package ru.practicum.shareit.user.dto;

import lombok.*;
import lombok.experimental.NonFinal;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Value
public class UserDtoCreate {
    @NonFinal
    Long id;
    @NonFinal
    String name;
    @Email
    @NotNull
    @NonFinal
    String email;


}
