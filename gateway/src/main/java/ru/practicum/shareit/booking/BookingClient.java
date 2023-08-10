package ru.practicum.shareit.booking;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.enums.State;

import java.util.HashMap;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(Integer userId, BookingDto bookingDto) {
        return post("", userId, bookingDto);
    }

    public ResponseEntity<Object> bookingConfirmation(Integer userId, Integer bookingId, Boolean approved) {
        String path = "/" + bookingId + "?approved=" + approved;
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("approved", approved);

        return patch(path, userId, parameters);
    }

    public ResponseEntity<Object> getBookingById(Integer userId, Integer bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllBookingsByUser(Integer userId, State state, Integer from, Integer size) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("state", state.name());
        parameters.put("from", from);
        parameters.put("size", size);

        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getAllBookingsByOwner(Integer userId, State state, Integer from, Integer size) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("state", state.name());
        parameters.put("from", from);
        parameters.put("size", size);

        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }
}