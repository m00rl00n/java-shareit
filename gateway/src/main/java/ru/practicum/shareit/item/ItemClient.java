package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.shareit.client.BaseClient;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ItemClient extends BaseClient {

    static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(WebClient webClient) {
        super(webClient);
    }

    public ResponseEntity<Object> create(Integer userId, ItemDto itemDto) {
        log.info("Добавление новой вещи");
        return post("", userId, itemDto).block();
    }

    public ResponseEntity<Object> comment(Integer userId, Integer itemId, CommentDto commentDto) {
        log.info("Добавление нового коментария");
        return post("/" + itemId + "/comment", userId, commentDto).block();
    }

    public ResponseEntity<Object> update(Integer userId, Integer itemId, ItemDto itemDto) {
        log.info("Обновление данных");
        return patch("/" + itemId, userId, itemDto).block();
    }

    public ResponseEntity<Object> getById(Integer userId, Integer itemId) {
        log.info("Просмотр информации");
        return get("/" + itemId, userId).block();
    }

    public ResponseEntity<Object> viewAllItems(Integer userId, Integer from, Integer size) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("from", from);
        parameters.put("size", size);
        log.info("Поиск всех вещей");
        String path = "?from={from}&size={size}";
        return get(path, userId, parameters).block();
    }

    public ResponseEntity<Object> search(String text, Integer userId, Integer from, Integer size) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("text", text);
        parameters.put("from", from);
        parameters.put("size", size);
        log.info("Поиск вещей");
        String path = "/search?text={text}&from={from}&size={size}";
        return get(path, userId, parameters).block();
    }
}