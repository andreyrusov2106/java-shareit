package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {
    public Long id;
    public String name;
    @Email
    @NotEmpty
    public String email;
}
