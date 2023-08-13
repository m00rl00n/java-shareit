package ru.practicum.shareit.booking;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.enums.State;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingClient extends BaseClient {

    static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(RestTemplate restTemplate) {
        super(restTemplate, API_PREFIX);
    }

    public ResponseEntity<Object> create(Integer userId, BookingDto bookingDto) {
        log.info("Создание бронирования");
        return post("", userId, bookingDto);
    }

    public ResponseEntity<Object> bookingConfirmation(Integer userId, Integer bookingId, Boolean approved) {
        String path = "/" + bookingId + "?approved=" + approved;
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("approved", approved);
        log.info("Подтверждение бронирования");

        return patch(path, userId, parameters);
    }

    public ResponseEntity<Object> getById(Integer userId, Integer bookingId) {
        log.info("Просмотр бронирования");
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllBookingsByOwner(Integer userId, State state, Integer from, Integer size) {
        var parameters = Map.<String, Object>of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        log.info("Просмотр всех бронирований");
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getAllBookingsByUser(Integer userId, State state, Integer from, Integer size) {
        var parameters = Map.<String, Object>of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        log.info("Просмотр бронирований");

        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }
}