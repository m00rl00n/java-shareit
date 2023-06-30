package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {
    final UserStorage userStorage;
    final UserDtoMapper userDtoMapper;


    @Autowired
    public UserServiceImpl(UserStorage userStorage, UserDtoMapper userDtoMapper) {
        this.userStorage = userStorage;
        this.userDtoMapper = userDtoMapper;
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Получение всех пользователей");
        List<User> users = userStorage.getAllUsers();
        return userDtoMapper.toDtoList(users);
    }

    @Override
    public UserDto getUser(Integer id) {
        log.info("Получение пользователя с id=" + id);
        return userDtoMapper.toDto(userStorage.getUser(id));
    }

    @Override
    public UserDto addUser(UserDto user) {
        log.info("Добавление нового пользователя");
        User userNew = userDtoMapper.toEntity(user);
        return userDtoMapper.toDto(userStorage.addUser(userNew));
    }

    @Override
    public UserDto updateUser(UserDto user, Integer id) {
        log.info("Обновление информации о пользователе id=" + id);
        User userNew = userStorage.getUser(id);
        return UserDtoMapper.toDto(userStorage.updateUser(id, UserDtoMapper.toEntity(user)));
    }

    @Override
    public void removeUser(Integer id) {
        log.info("Удаление пользователя id=" + id);
        userStorage.removeUser(id);
    }
}
