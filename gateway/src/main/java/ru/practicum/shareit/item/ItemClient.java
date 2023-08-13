package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemClient extends BaseClient {

    static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("from", from);
        parameters.put("size", size);
        log.info("Поиск всех вещей");
        String path = "?from={from}&size={size}";
        return get(path, userId, parameters);
    }

    public ResponseEntity<Object> search(String text, Integer userId, Integer from, Integer size) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("text", text);
        parameters.put("from", from);
        parameters.put("size", size);
        log.info("Поиск вещей");
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }
}
