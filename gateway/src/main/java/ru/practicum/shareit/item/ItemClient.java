package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemClient extends BaseClient {

    static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(RestTemplate restTemplate) {
        super(restTemplate, API_PREFIX);
    }

    public ResponseEntity<Object> create(Integer userId, ItemDto itemDto) {
        log.info("Добавление новой вещи");
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> comment(Integer userId, Integer itemId, CommentDto commentDto) {
        log.info("Добавление нового коментария");
        return post("/" + itemId + "/comment", userId, commentDto);
    }

    public ResponseEntity<Object> update(Integer userId, Integer itemId, ItemDto itemDto) {
        log.info("Обновление данных");
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> getById(Integer userId, Integer itemId) {
        log.info("Просмотр информации");
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> viewAllItems(Integer userId, Integer from, Integer size) {
        var parameters = Map.<String, Object>of(
                "from", from,
                "size", size
        );
        log.info("Поиск всех вещей");
        String path = "?from={from}&size={size}";
        return get(path, userId, parameters);
    }

    public ResponseEntity<Object> search(String text, Integer userId, Integer from, Integer size) {
        var parameters = Map.<String, Object>of(
                "text", text,
                "from", from,
                "size", size
        );
        log.info("Поиск вещей");
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }
}
