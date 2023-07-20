package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoReceived;
import ru.practicum.shareit.booking.dto.BookingDtoReturned;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    BookingDtoReturned create(BookingDtoReceived bookingDto, Integer id);

    BookingDtoReturned bookingConfirmation(Integer bookingId, Boolean confirmation, Integer id);

    BookingDtoReturned getBookingById(Integer bookingId, Integer id);

    List<BookingDtoReturned> getAllBookingsByUser(String state, Integer id);

    List<BookingDtoReturned> getAllBookingsByOwner(String state, Integer id);
}