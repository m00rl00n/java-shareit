package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getAllUsers();

    User getUser(Integer id);

    User add(User user);

    User update(Integer id, User user);

    void remove(Integer id);
}
