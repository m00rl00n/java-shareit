package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.enums.State;
import ru.practicum.shareit.exception.UnsupportedStateException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingController {

    final BookingClient client;

    @Valid
    @PostMapping()
    ResponseEntity<Object> create(@RequestBody BookingDto bookingDto,
                                  @RequestHeader("X-Sharer-User-Id") Integer userId) {
        BookingDto.validate(bookingDto);
        return client.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    ResponseEntity<Object> bookingConfirmation(@PathVariable Integer bookingId, @RequestParam Boolean approved,
                                               @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return client.bookingConfirmation(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    ResponseEntity<Object> getById(@PathVariable Integer bookingId,
                                   @RequestHeader("X-Sharer-User-Id") Integer userId) {

        return client.getById(userId, bookingId);
    }

    @GetMapping()
    ResponseEntity<Object> getAllBookingsByUser(@RequestParam(name = "state", defaultValue = "ALL") String stateString,
                                                @RequestHeader("X-Sharer-User-Id") Integer userId,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        State state = State.from(stateString)
                .orElseThrow(() -> new UnsupportedStateException("Unknown state: " + stateString));
        return client.getAllBookingsByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    ResponseEntity<Object> getAllBookingsByOwner(@RequestParam(name = "state", defaultValue = "ALL") String stateString,
                                                 @RequestHeader("X-Sharer-User-Id") Integer userId,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(defaultValue = "10") @Positive Integer size) {
        State state = State.from(stateString)
                .orElseThrow(() -> new UnsupportedStateException("Unknown state: " + stateString));
        return client.getAllBookingsByOwner(userId, state, from, size);
    }
}