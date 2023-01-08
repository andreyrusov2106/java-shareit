package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {

        return itemClient.getItem(itemId, userId);
    }

    @GetMapping("")
    public ResponseEntity<Object> findAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all items");
        return itemClient.findAll(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody @Valid ItemDtoCreate itemDto) {
        log.info("Creating item {}", itemDto);
        return itemClient.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody @Valid ItemDtoUpdate itemDto) {
        log.info("Updating item {}, itemId={}", itemId, itemDto);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> removeItem(@PathVariable Long itemId) {
        log.info("Deleting item with itemId={}", itemId);
        return itemClient.removeItem(itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam(name = "text") String text) {
        log.info("Searching item with text is {}", text);
        return itemClient.searchItems(text);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestBody @Valid CommentDto commentDto,
                                                @RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long itemId) {
        log.info("Adding comment {} to item with id {}", commentDto, itemId);
        return itemClient.createComment(userId, itemId, commentDto);
    }
}
