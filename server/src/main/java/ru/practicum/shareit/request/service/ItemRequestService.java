package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long userId);

    ItemRequestDto getItemRequest(Long id, Long userId);

    List<ItemRequestDto> getAllItemRequest(Long userId);

    List<ItemRequestDto> getAllItemRequestWithPagination(Long userId, Integer from, Integer size);
}
