package ru.practicum.shareit.request.dto;

import lombok.*;
import lombok.experimental.NonFinal;
import ru.practicum.shareit.user.dto.UserDtoCreate;

import javax.validation.constraints.NotEmpty;
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
    List<ItemRequestDto> items;


}
