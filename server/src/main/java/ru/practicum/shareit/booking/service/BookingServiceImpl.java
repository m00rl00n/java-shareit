package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.List;


@Slf4j
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
        log.info("Создание бронирования");
        checkOwnerCannotCreateBooking(item, user);
        checkItemAvailability(item);
        checkBookingDates(bookingDto.getStart(), bookingDto.getEnd());

        Booking booking = createBooking(bookingDto, user, item);
        return BookingDtoMapper.toBookingDtoReturned(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoReturned bookingConfirmation(Integer bookingId, Boolean confirmation, Integer userId) {
        Booking booking = getBookingById(bookingId);
        User user = getUserById(userId);
        log.info("Подтверждение бронирования");
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
        Booking booking = getBookingByIdAndUser(bookingId, user);
        log.info("Просмотр бронирования с айди ");
        return BookingDtoMapper.toBookingDtoReturned(booking);
    }

    @Override
    public List<BookingDtoReturned> getAllBookingsByUser(String state, Integer id, Integer from, Integer size) {
        final Sort sort = Sort.by(Sort.Direction.DESC, "start");
        User user = getUserById(id);
        log.info("Просмотр бронирований пользователем");

        Pageable pageable = PageRequest.of(from / size, size, sort);
        Page<Booking> bookingPage = getBookingsByUserAndState(state, user, pageable);
        List<Booking> bookings = bookingPage.getContent();

        List<BookingDtoReturned> bookingDtoList = new ArrayList<>();

        for (Booking booking : bookings) {
            bookingDtoList.add(BookingDtoMapper.toBookingDtoReturned(booking));
        }

        return bookingDtoList;
    }

    @Override
    public List<BookingDtoReturned> getAllBookingsByOwner(String state, Integer id, Integer from, Integer size) {
        final Sort sort = Sort.by(Sort.Direction.DESC, "start");
        User user = getUserById(id);
        log.info("Просмотр бронирований");

        Pageable pageable = PageRequest.of(from / size, size, sort);
        Page<Booking> bookingPage = getBookingsByOwnerAndState(state, user, pageable);
        List<Booking> bookings = bookingPage.getContent();

        List<BookingDtoReturned> bookingDtoList = new ArrayList<>();

        for (Booking booking : bookings) {
            bookingDtoList.add(BookingDtoMapper.toBookingDtoReturned(booking));
        }

        return bookingDtoList;
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

    private Booking getBookingByIdAndUser(Integer bookingId, User user) {
        Booking booking = getBookingById(bookingId);
        if (!user.getId().equals(booking.getItem().getOwner().getId()) &&
                !user.getId().equals(booking.getBooker().getId())) {
            throw new NotFoundException("Бронирование недоступно");
        }
        return booking;
    }

    private Page<Booking> getBookingsByUserAndState(String state, User user, Pageable pageable) {
        switch (state.toUpperCase()) {
            case "ALL":
                return bookingRepository.findAllByBooker(user, pageable);
            case "FUTURE":
                return bookingRepository.findAllByBookerAndStartAfter(user, LocalDateTime.now(), pageable);
            case "CURRENT":
                return bookingRepository.findAllByBookerAndStartBeforeAndEndAfter(user, LocalDateTime.now(),
                        LocalDateTime.now(), pageable);
            case "PAST":
                return bookingRepository.findAllByBookerAndEndBefore(user, LocalDateTime.now(), pageable);
            case "WAITING":
                return bookingRepository.findAllByBookerAndStatusEquals(user, Status.WAITING, pageable);
            case "REJECTED":
                return bookingRepository.findAllByBookerAndStatusEquals(user, Status.REJECTED, pageable);
            default:
                throw new UnsupportedStateException("Unknown state: " + state);
        }
    }

    private Page<Booking> getBookingsByOwnerAndState(String state, User user, Pageable pageable) {
        switch (state.toUpperCase()) {
            case "ALL":
                return bookingRepository.findAllByItemOwner(user, pageable);
            case "FUTURE":
                return bookingRepository.findAllByItemOwnerAndStartAfter(user, LocalDateTime.now(), pageable);
            case "CURRENT":
                return bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfter(user, LocalDateTime.now(),
                        LocalDateTime.now(), pageable);
            case "PAST":
                return bookingRepository.findAllByItemOwnerAndEndBefore(user, LocalDateTime.now(), pageable);
            case "WAITING":
                return bookingRepository.findAllByItemOwnerAndStatusEquals(user, Status.WAITING, pageable);
            case "REJECTED":
                return bookingRepository.findAllByItemOwnerAndStatusEquals(user, Status.REJECTED, pageable);
            default:
                throw new UnsupportedStateException("Unknown state: " + state);
        }
    }

    private void checkUserIsOwnerOfItem(User user, Item item) {
        if (!item.getOwner().getId().equals(user.getId())) {
            throw new NotFoundException("Пользователь не может изменять статус бронирования");
        }
    }

    private BookingDtoReturned saveAndReturnBookingDtoReturned(Booking booking) {
        return BookingDtoMapper.toBookingDtoReturned(bookingRepository.save(booking));
    }
}