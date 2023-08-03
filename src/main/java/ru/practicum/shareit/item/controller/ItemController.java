package ru.practicum.shareit.item.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOwner;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemController {
    final ItemServiceImpl service;

    @PostMapping
    public ItemDto add(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer id) {
        return service.add(itemDto, id);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto commentItem(@PathVariable Integer itemId, @RequestBody CommentDto commentDto,
                                  @RequestHeader("X-Sharer-User-Id") Integer id) {
        return service.commentItem(itemId, commentDto, id);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable Integer itemId, @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") Integer id) {
        return service.update(itemId, itemDto, id);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOwner getItem(@PathVariable Integer itemId, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return service.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoOwner> getAllItems(@RequestHeader("X-Sharer-User-Id") Integer id) {
        return service.getItemsByUserId(id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return service.search(text);
    }
}