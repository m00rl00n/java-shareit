package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    final UserClient userClient;

    @PostMapping
    ResponseEntity<Object> create(@RequestBody @Validated(ru.practicum.shareit.client.Validated.Create.class) UserDto userDto) {
        return userClient.create(userDto);
    }


    @GetMapping("/{userId}")
    ResponseEntity<Object> getById(@PathVariable Integer userId) {
        return userClient.getById(userId);
    }

    @PatchMapping("/{userId}")
    ResponseEntity<Object> update(@PathVariable Integer userId, @RequestBody @Validated(ru.practicum.shareit.client.Validated.Update.class) UserDto userDto) {
        return userClient.update(userId, userDto);
    }

    @GetMapping
    ResponseEntity<Object> getAll() {
        return userClient.getAll();
    }

    @DeleteMapping("/{userId}")
    void delete(@PathVariable Integer userId) {
        userClient.delete(userId);
    }
}
