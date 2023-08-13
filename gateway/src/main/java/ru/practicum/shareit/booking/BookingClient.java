package ru.practicum.shareit.booking;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.enums.State;

import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class BookingClient extends BaseClient {

    public static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(WebClient webClient) {
        super(webClient);
    }

    public ResponseEntity<Object> create(Integer userId, BookingDto bookingDto) {
        log.info("Создание бронирования");
        return post("", userId, bookingDto).block();
    }

    public ResponseEntity<Object> bookingConfirmation(Integer userId, Integer bookingId, Boolean approved) {
        String path = "/" + bookingId + "?approved=" + approved;
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("approved", approved);
        log.info("Подтверждение бронирования");

        return patch(path, userId, parameters).block();
    }

    public ResponseEntity<Object> getById(Integer userId, Integer bookingId) {
        log.info("Просмотр бронирования");
        return get("/" + bookingId, userId).block();
    }

    public ResponseEntity<Object> getAllBookingsByOwner(Integer userId, State state, Integer from, Integer size) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("state", state.name());
        parameters.put("from", from);
        parameters.put("size", size);
        log.info("Просмотр бронирований");

        return get("/owner?state={state}&from={from}&size={size}", userId, parameters).block();
    }

    public ResponseEntity<Object> getAllBookingsByUser(Integer userId, State state, Integer from, Integer size) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("state", state.name());
        parameters.put("from", from);
        parameters.put("size", size);
        log.info("Просмотр бронирований");

        return get("?state={state}&from={from}&size={size}", userId, parameters).block();
    }
}
