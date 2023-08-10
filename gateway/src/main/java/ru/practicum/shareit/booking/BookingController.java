package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.enums.State;
import ru.practicum.shareit.exception.UnsupportedStateException;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingController {

	final BookingClient client;

	@Valid
	@PostMapping()
	ResponseEntity<Object> createBooking(@RequestBody BookingDto bookingDto,
										 @RequestHeader("X-Sharer-User-Id") Integer userId) {
		log.info("Создание нового бронирования пользователем с айди " + userId);
		BookingDto.validate(bookingDto);
		return client.createBooking(userId, bookingDto);
	}

	@PatchMapping("/{bookingId}")
	ResponseEntity<Object> bookingConfirmation(@PathVariable Integer bookingId, @RequestParam Boolean approved,
											   @RequestHeader("X-Sharer-User-Id") Integer userId) {
		log.info("Подтверждение бронирования с айди " + bookingId + " владельцем вещи");
		return client.bookingConfirmation(userId, bookingId, approved);
	}

	@GetMapping("/{bookingId}")
	ResponseEntity<Object> getBookingById(@PathVariable Integer bookingId,
										  @RequestHeader("X-Sharer-User-Id") Integer userId) {
		log.info("Просмотр бронирования с айди " + bookingId);
		return client.getBookingById(userId, bookingId);
	}

	@GetMapping()
	ResponseEntity<Object> getAllBookingsByUser(@RequestParam(name = "state", defaultValue = "ALL") String stateString,
												@RequestHeader("X-Sharer-User-Id") Integer userId,
												@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
												@RequestParam(defaultValue = "10") @Positive Integer size) {
		log.info("Просмотр бронирований со статусом " + stateString + " пользователем с айди " + userId +
				" , from={}, size={}", from, size);
		State state = State.from(stateString)
				.orElseThrow(() -> new UnsupportedStateException("Unknown state: " + stateString));
		return client.getAllBookingsByUser(userId, state, from, size);
	}

	@GetMapping("/owner")
	ResponseEntity<Object> getAllBookingsByOwner(@RequestParam(name = "state", defaultValue = "ALL") String stateString,
												 @RequestHeader("X-Sharer-User-Id") Integer userId,
												 @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
												 @RequestParam(defaultValue = "10") @Positive Integer size) {
		log.info("Просмотр бронирований со статусом " + stateString + " владельцем с айди " + userId +
				" , from={}, size={}", from, size);
		State state = State.from(stateString)
				.orElseThrow(() -> new UnsupportedStateException("Unknown state: " + stateString));
		return client.getAllBookingsByOwner(userId, state, from, size);
	}
}