package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping("")
    public ResponseEntity<Object> createItemRequest(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.createItemRequest(itemRequestDto, userId);
    }


    @GetMapping()
    public ResponseEntity<Object> findAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.findAll(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return itemRequestClient.findAllWithPagination(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getItemRequest(userId, itemId);
    }


}
