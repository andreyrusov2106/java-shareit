package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@Value
public class UserDto {
    Long id;
    String name;
    @Email
    @NotEmpty
    String email;
}
