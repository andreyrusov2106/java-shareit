package ru.practicum.shareit.request.dto;

import lombok.*;
import lombok.experimental.NonFinal;
import ru.practicum.shareit.user.dto.UserDtoCreate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Value
public class ItemRequestDto {
    @NonFinal
    Long id;
    @NotEmpty
    @NonFinal
    String description;
    @NonFinal
    UserDtoCreate requester;
    @NonFinal
    LocalDateTime created;
    @NonFinal
    @Setter
    List<ItemRequestDto> items;


}
