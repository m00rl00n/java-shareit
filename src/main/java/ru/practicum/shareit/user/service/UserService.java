package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUser(Integer id);

    UserDto add(UserDto user);

    UserDto update(UserDto user, Integer id);

    void remove(Integer id);
}
