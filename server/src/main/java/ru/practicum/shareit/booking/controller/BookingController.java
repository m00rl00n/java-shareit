package ru.practicum.shareit.booking.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoReceived;
import ru.practicum.shareit.booking.dto.BookingDtoReturned;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingController {
    final BookingServiceImpl bookingService;

    @PostMapping
    public BookingDtoReturned create(@RequestBody BookingDtoReceived bookingDto,
                                     @RequestHeader("X-Sharer-User-Id") Integer id) {
        return bookingService.create(bookingDto, id);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoReturned bookingConfirmation(@PathVariable Integer bookingId, @RequestParam Boolean approved,
                                                  @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.bookingConfirmation(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoReturned getBooking(@PathVariable Integer bookingId,
                                         @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    List<BookingDtoReturned> getAllBookingsByUser(@RequestParam(defaultValue = "ALL") String state,
                                                  @RequestHeader("X-Sharer-User-Id") Integer id,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.getAllBookingsByUser(state, id, from, size);
    }

    @GetMapping("/owner")
    List<BookingDtoReturned> getAllBookingsByOwner(@RequestParam(defaultValue = "ALL") String state,
                                                   @RequestHeader("X-Sharer-User-Id") Integer id,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.getAllBookingsByOwner(state, id, from, size);
    }
}