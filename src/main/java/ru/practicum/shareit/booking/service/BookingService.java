package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoReceived;
import ru.practicum.shareit.booking.dto.BookingDtoReturned;

import java.util.List;

public interface BookingService {
    BookingDtoReturned create(BookingDtoReceived bookingDto, Integer id);

    BookingDtoReturned bookingConfirmation(Integer bookingId, Boolean confirmation, Integer id);

    BookingDtoReturned getBookingById(Integer bookingId, Integer id);

    List<BookingDtoReturned> getAllBookingsByUser(String state, Integer userId, Integer from, Integer size);

    List<BookingDtoReturned> getAllBookingsByOwner(String state, Integer ownerId, Integer from, Integer size);
}