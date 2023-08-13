package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReceived;
import ru.practicum.shareit.booking.dto.BookingDtoReturned;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingDtoMapper {
    public static Booking toBooking(BookingDtoReceived bookingDto, User user, Item item) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }

    public static BookingDtoReturned toBookingDtoReturned(Booking booking) {
        BookingDtoReturned bookingDtoReturned = new BookingDtoReturned();
        bookingDtoReturned.setId(booking.getId());
        bookingDtoReturned.setStart(booking.getStart());
        bookingDtoReturned.setEnd(booking.getEnd());
        bookingDtoReturned.setItem(booking.getItem());
        bookingDtoReturned.setBooker(booking.getBooker());
        bookingDtoReturned.setStatus(booking.getStatus());
        return bookingDtoReturned;
    }

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItemId(booking.getItem().getId());
        bookingDto.setBookerId(booking.getBooker().getId());
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    public static BookingDtoReceived toBookingDtoReceived(Booking booking) {
        BookingDtoReceived bookingDtoReceived = new BookingDtoReceived();
        bookingDtoReceived.setId(booking.getId());
        bookingDtoReceived.setStart(booking.getStart());
        bookingDtoReceived.setEnd(booking.getEnd());
        bookingDtoReceived.setItemId(booking.getItem().getId());
        bookingDtoReceived.setBooker(booking.getBooker());
        bookingDtoReceived.setStatus(booking.getStatus());
        return bookingDtoReceived;
    }
}


