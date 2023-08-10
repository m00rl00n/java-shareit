package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestController {
    final RequestClient client;

    @PostMapping
    ResponseEntity<Object> create(@RequestBody ItemRequestDto itemRequestDto,
                                  @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return client.create(userId, itemRequestDto);
    }

    @GetMapping
    ResponseEntity<Object> getUserRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return client.getUserRequests(userId);
    }

    @GetMapping("/all")
    ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        return client.getAll(userId, from, size);

    }

    @GetMapping("/{requestId}")
    ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                   @PathVariable Integer requestId) {
        return client.getById(userId, requestId);
    }
}
