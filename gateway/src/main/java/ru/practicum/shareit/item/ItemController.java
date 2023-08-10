package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/items")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemController {

    final ItemClient client;

    @PostMapping
    ResponseEntity<Object> create(@RequestBody @Validated(ru.practicum.shareit.client.Validated.Create.class) ItemDto itemDto,
                                  @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return client.create(userId, itemDto);
    }

    @PostMapping("{itemId}/comment")
    ResponseEntity<Object> comment(@PathVariable Integer itemId, @RequestBody CommentDto commentDto,
                                   @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return client.comment(userId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")
    ResponseEntity<Object> update(@PathVariable Integer itemId, @RequestBody @Validated(ru.practicum.shareit.client.Validated.Update.class) ItemDto itemDto,
                                  @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return client.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    ResponseEntity<Object> getById(@PathVariable Integer itemId,
                                   @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return client.getById(userId, itemId);
    }

    @GetMapping
    ResponseEntity<Object> viewAllItems(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = "10") @Positive Integer size) {
        return client.viewAllItems(userId, from, size);
    }

    @GetMapping("/search")
    ResponseEntity<Object> search(@RequestParam String text, @RequestHeader("X-Sharer-User-Id") Integer userId,
                                  @RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size) {
        return client.search(text, userId, from, size);
    }
}
