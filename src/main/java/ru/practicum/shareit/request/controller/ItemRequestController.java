package ru.practicum.shareit.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping()
    public ItemRequestDto createItemRequest(@RequestBody ItemRequestDto itemRequestDto,
                                            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping()
    public List<ItemRequestDto> findAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAllItemRequest(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(name = "from", required = false) Integer from,
                                            @RequestParam(name = "size", required = false) Integer size) {
        return itemRequestService.getAllItemRequestWithPagination(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemRequestDto getItem(@PathVariable Long itemId,
                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getItemRequest(itemId, userId);
    }
}
