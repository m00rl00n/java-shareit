package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getAllUsers();

    User getUser(Integer id);

    User addUser(User user);

    User updateUser(Integer id, User user);

    void removeUser(Integer id);
}
