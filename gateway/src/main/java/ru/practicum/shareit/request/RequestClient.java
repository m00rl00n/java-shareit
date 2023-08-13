package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestClient extends BaseClient {

    static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(RestTemplate restTemplate) {
        super(restTemplate, API_PREFIX);
    }

    public ResponseEntity<Object> create(Integer userId, ItemRequestDto requestDto) {
        log.info("Создание запроса");
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getUserRequests(Integer userId) {
        log.info("Просмотр запросов");
        return get("", userId);
    }

    public ResponseEntity<Object> getAll(Integer userId, Integer from, Integer size) {
        var parameters = Map.<String, Object>of(
                "from", from,
                "size", size
        );
        log.info("Просмотр всех запросов");

        return get("/all?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getById(Integer userId, Integer requestId) {
        log.info("Просмотр запроса");
        return get("/" + requestId, userId);
    }
}