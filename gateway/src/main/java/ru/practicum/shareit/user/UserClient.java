package ru.practicum.shareit.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.shareit.client.BaseClient;

@Component
@Slf4j
public class UserClient extends BaseClient {

    static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(WebClient webClient) {
        super(webClient);
    }

    public ResponseEntity<Object> create(UserDto userDto) {
        log.info("Добавление нового пользователя");
        return post("", userDto).block();
    }

    public ResponseEntity<Object> getById(Integer userId) {
        log.info("Пользователь с айди " + userId);
        return get("/" + userId).block();
    }

    public ResponseEntity<Object> getAll() {
        log.info("Получение всех пользователей");
        return get("").block();
    }

    public ResponseEntity<Object> update(Integer userId, UserDto userDto) {
        log.info("Обновление");
        return patch("/" + userId, userDto).block();
    }

    public ResponseEntity<Object> delete(Integer userId) {
        log.info("Удаление пользователь с айди " + userId);
        return delete("/" + userId).block();
    }
}