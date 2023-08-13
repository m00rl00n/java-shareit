package ru.practicum.shareit.user;

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

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserClient extends BaseClient {

    static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(UserDto userDto) {
        log.info("Добавление нового пользователя");
        return post("", userDto);
    }

    public ResponseEntity<Object> getById(Integer userId) {
        log.info("Пользователь с айди " + userId);
        return get("/" + userId);
    }

    public ResponseEntity<Object> getAll() {
        log.info("Получение всех пользователей");
        return get("");
    }

    public ResponseEntity<Object> update(Integer userId, UserDto userDto) {
        log.info("Обновление");
        return patch("/" + userId, userDto);
    }

    public ResponseEntity<Object> delete(Integer userId) {
        log.info("Удаление пользователь с айди " + userId);
        return delete("/" + userId);
    }
}