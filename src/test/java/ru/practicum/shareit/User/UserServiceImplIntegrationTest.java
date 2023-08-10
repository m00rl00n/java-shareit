package ru.practicum.shareit.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceImplIntegrationTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getAllUsersTest() {

        UserDto user1 = new UserDto(null, "Маша1", "masha1@yandex.ru");
        UserDto user2 = new UserDto(null, "Маша2", "masha2@yandex.ru");
        userService.add(user1);
        userService.add(user2);

        List<UserDto> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void getUserTest() {

        UserDto userDto = new UserDto(null, "Маша1", "masha1@yandex.ru");
        userDto = userService.add(userDto);

        UserDto retrievedUser = userService.getUser(userDto.getId());
        assertNotNull(retrievedUser);
        assertEquals(userDto.getEmail(), retrievedUser.getEmail());
        assertEquals(userDto.getName(), retrievedUser.getName());
    }

    @Test
    void addUserTest() {
        UserDto userDto = new UserDto(null, "Маша1", "masha1@yandex.ru");
        UserDto addedUser = userService.add(userDto);

        assertNotNull(addedUser.getId());
        assertEquals(userDto.getEmail(), addedUser.getEmail());
        assertEquals(userDto.getName(), addedUser.getName());
    }

    @Test
    void updateUserTest() {
        UserDto userDto = new UserDto(null, "Маша1", "masha1@yandex.ru");
        userDto = userService.add(userDto);
        userDto.setName("updated_user");
        userDto.setEmail("updated_email@yandex.ru");

        UserDto updatedUser = userService.update(userDto, userDto.getId());

        assertEquals(userDto.getId(), updatedUser.getId());
        assertEquals("updated_user", updatedUser.getName());
        assertEquals("updated_email@yandex.ru", updatedUser.getEmail());
    }

    @Test
    void removeUserTest() {
        final UserDto userDto = new UserDto(null, "Маша", "masha@example.com");
        final UserDto addedUser = userService.add(userDto);

        userService.remove(addedUser.getId());

        assertThrows(NotFoundException.class, () -> userService.getUser(addedUser.getId()));
    }

    @Test
    void addUserWithBlankEmailTest() {
        final UserDto userDto = new UserDto(null, "Маша", "");
        assertThrows(ValidationException.class, () -> userService.add(userDto));
    }

    @Test
    void addUserWithInvalidEmailTest() {
        final UserDto userDto = new UserDto(null, "Маша", "invalid_email");
        assertThrows(ValidationException.class, () -> userService.add(userDto));
    }

    @Test
    void updateUserWithInvalidEmailTest() {
        final UserDto userDto = new UserDto(null, "Маша1", "masha1@yandex.ru");
        final UserDto addedUser = userService.add(userDto);

        final UserDto updatedUser = new UserDto(addedUser.getId(), "Маша1", "invalid_email");
        assertThrows(ValidationException.class, () -> userService.update(updatedUser, addedUser.getId()));
    }
}

