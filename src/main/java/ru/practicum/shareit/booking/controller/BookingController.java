package ru.practicum.shareit.booking.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoReceived;
import ru.practicum.shareit.booking.dto.BookingDtoReturned;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingController {
    final BookingServiceImpl bookingService;

    @PostMapping
    public BookingDtoReturned createBooking(@RequestBody BookingDtoReceived bookingDto,
                                            @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoReturned bookingConfirmation(@PathVariable Integer bookingId, @RequestParam Boolean approved,
                                                  @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.bookingConfirmation(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoReturned getBookingById(@PathVariable Integer bookingId,
                                             @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDtoReturned> getAllBookingsByUser(@RequestParam(defaultValue = "ALL") String state,
                                                         @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.getAllBookingsByUser(state, userId);
    }

    @GetMapping("/owner")
    public List<BookingDtoReturned> getAllBookingsByOwner(@RequestParam(defaultValue = "ALL") String state,
                                                          @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.getAllBookingsByOwner(state, userId);
    }
}