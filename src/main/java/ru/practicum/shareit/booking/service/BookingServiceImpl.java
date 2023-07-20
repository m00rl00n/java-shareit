package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoReceived;
import ru.practicum.shareit.booking.dto.BookingDtoReturned;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStateException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingServiceImpl implements BookingService {
    final BookingRepository bookingRepository;
    final ItemRepository itemRepository;
    final UserRepository userRepository;

    @Override
    public BookingDtoReturned create(BookingDtoReceived bookingDto, Integer id) {
        User user = getUserById(id);
        Item item = getItemById(bookingDto.getItemId());
        checkOwnerCannotCreateBooking(item, user);
        checkItemAvailability(item);
        checkBookingDates(bookingDto.getStart(), bookingDto.getEnd());

        Booking booking = createBooking(bookingDto, user, item);
        return saveAndReturnBookingDtoReturned(booking);
    }

    @Override
    public BookingDtoReturned bookingConfirmation(Integer bookingId, Boolean confirmation, Integer userId) {
        Booking booking = getBookingById(bookingId);
        User user = getUserById(userId);
        checkUserIsOwnerOfItem(user, booking.getItem());

        if (booking.getStatus().equals(Status.WAITING) && confirmation) {
            booking.setStatus(Status.APPROVED);
        } else if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException("Вещь уже доступна для бронирования");
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return saveAndReturnBookingDtoReturned(booking);
    }

    @Override
    public BookingDtoReturned getBookingById(Integer bookingId, Integer userId) {
        User user = getUserById(userId);
        Booking booking = getBookingById(bookingId);

        if (!user.getId().equals(booking.getItem().getOwner().getId()) &&
                !user.getId().equals(booking.getBooker().getId())) {
            throw new NotFoundException("Бронирование недоступно");
        }

        return BookingDtoMapper.toBookingDtoReturned(booking);
    }

    @Override
    public List<BookingDtoReturned> getAllBookingsByUser(String state, Integer id) {
        User user = getUserById(id);
        List<Booking> bookings = bookingRepository.findBookingsByBookerId(user.getId());
        List<Booking> filteredBookings = filterBookingsByState(state, bookings);
        return mapToBookingDtoReturned(filteredBookings);
    }

    @Override
    public List<BookingDtoReturned> getAllBookingsByOwner(String state, Integer id) {
        User user = getUserById(id);
        List<Booking> bookings = bookingRepository.findBookingsByItemOwnerId(user.getId());
        List<Booking> filteredBookings = filterBookingsByState(state, bookings);
        return mapToBookingDtoReturned(filteredBookings);
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private Item getItemById(Integer itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
    }

    private void checkOwnerCannotCreateBooking(Item item, User user) {
        if (item.getOwner().getId().equals(user.getId())) {
            throw new NotFoundException("Владелец вещи не может создать ее бронирование");
        }
    }

    private void checkItemAvailability(Item item) {
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь с айди " + item.getId() + " недоступна для бронирования");
        }
    }

    private void checkBookingDates(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new ValidationException("Нужно указать начало и конец брони");
        }
        if (start.isBefore(LocalDateTime.now()) || end.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Бронь не может начаться или закончиться раньше текущей даты и времени");
        }
        if (end.isBefore(start) || end.isEqual(start)) {
            throw new ValidationException("Бронь не должна заканчиваться одновременно с началом");
        }
    }

    private Booking createBooking(BookingDtoReceived bookingDto, User user, Item item) {
        Booking booking = BookingDtoMapper.toBooking(bookingDto, user, item);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);
        return booking;
    }

    private Booking getBookingById(Integer bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
    }

    private void checkUserIsOwnerOfItem(User user, Item item) {
        if (!item.getOwner().getId().equals(user.getId())) {
            throw new NotFoundException("Пользователь не может изменять статус бронирования");
        }
    }

    private BookingDtoReturned saveAndReturnBookingDtoReturned(Booking booking) {
        return BookingDtoMapper.toBookingDtoReturned(bookingRepository.save(booking));
    }

    private List<Booking> filterBookingsByState(String state, List<Booking> bookings) {
        List<Booking> filteredBookings = new ArrayList<>();

        for (Booking booking : bookings) {
            if (stateMatches(booking, state)) {
                filteredBookings.add(booking);
            }
        }

        filteredBookings.sort(Comparator.comparing(Booking::getStart).reversed());
        return filteredBookings;
    }

    private boolean stateMatches(Booking booking, String state) {
        switch (state.toUpperCase()) {
            case "ALL":
                return true;
            case "FUTURE":
                return booking.getStart().isAfter(LocalDateTime.now());
            case "CURRENT":
                return booking.getStart().isBefore(LocalDateTime.now()) &&
                        booking.getEnd().isAfter(LocalDateTime.now());
            case "PAST":
                return booking.getEnd().isBefore(LocalDateTime.now());
            case "WAITING":
                return booking.getStatus().equals(Status.WAITING);
            case "REJECTED":
                return booking.getStatus().equals(Status.REJECTED);
            default:
                throw new UnsupportedStateException("Unknown state: " + state);
        }
    }

    private List<BookingDtoReturned> mapToBookingDtoReturned(List<Booking> bookings) {
        List<BookingDtoReturned> bookingDtos = new ArrayList<>();

        for (Booking booking : bookings) {
            bookingDtos.add(BookingDtoMapper.toBookingDtoReturned(booking));
        }

        return bookingDtos;
    }
}