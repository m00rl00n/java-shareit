package ru.practicum.shareit.item.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOwner;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequestMapping("/items")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
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
    public List<ItemDtoOwner> getAllItems(@RequestHeader("X-Sharer-User-Id") Integer id,
                                          @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                          @RequestParam(defaultValue = "10") @Positive Integer size) {
        return service.getItemsByUserId(id, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text, @RequestHeader("X-Sharer-User-Id")
    @RequestParam(defaultValue = "0") Integer from,
                                     @RequestParam(defaultValue = "10") Integer size) {
        return service.search(text, from, size);
    }

}