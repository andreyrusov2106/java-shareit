package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();

    }

    public static void toItemRequest(ItemRequest itemRequest, ItemRequestDto itemRequestDto) {
        if (itemRequestDto.getId() != null) itemRequest.setId(itemRequestDto.getId());
        if (itemRequestDto.getDescription() != null) itemRequest.setDescription(itemRequestDto.getDescription());
    }
}
