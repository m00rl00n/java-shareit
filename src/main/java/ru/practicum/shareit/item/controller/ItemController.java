package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    static final String USER_ID_REQUEST_HEADER = "X-Sharer-User-Id";
    final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader(value = USER_ID_REQUEST_HEADER) Integer userId) {
        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(
            @RequestHeader(value = USER_ID_REQUEST_HEADER)
            @PathVariable Integer itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByText(
            @RequestHeader(value = USER_ID_REQUEST_HEADER) Integer userId,
            @RequestParam String text) {
        return itemService.searchItems(userId, text);
    }

    @PostMapping
    public ItemDto addItem(
            @RequestHeader(value = USER_ID_REQUEST_HEADER) Integer userId,
            @RequestBody ItemDto itemDto) {
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestHeader(value = USER_ID_REQUEST_HEADER) Integer userId,
            @PathVariable Integer itemId,
            @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }
}
