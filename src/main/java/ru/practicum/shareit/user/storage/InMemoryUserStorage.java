package ru.practicum.shareit.user.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InMemoryUserStorage implements UserStorage {

    static final String EMAIL_PATTERN = "\\w[\\w.-]*@\\w[\\w.-]*\\w{2,4}";

    final Map<Integer, User> users = new HashMap<>();
    Integer id = 0;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Integer id) {
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        validateUser(user);
        emailValidator(user.getId(), user.getEmail());
        user.setId(++id);
        users.put(id, user);
        return user;
    }

    @Override
    public User updateUser(Integer userId, User user) {
        User existingUser = users.get(userId);
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            validateUserEmail(user.getEmail());
            emailValidator(userId, user.getEmail());
            checkEmail(user.getEmail(), userId);
            existingUser.setEmail(user.getEmail());
        }
        return existingUser;
    }

    @Override
    public void removeUser(Integer id) {
        users.remove(id);
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        }
        validateUserEmail(user.getEmail());
    }

    private void validateUserEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email не может быть пустым");
        }
    }

    private void emailValidator(Integer userId, String email) {
        if (!email.matches(EMAIL_PATTERN)) {
            throw new ValidationException("Проверьте корректность почты");
        }
        checkEmail(email, userId);
    }

    private void checkEmail(String email, Integer userId) {
        for (User existingUser : users.values()) {
            if (existingUser.getEmail().equals(email) && !existingUser.getId().equals(userId)) {
                throw new ConflictException("Пользователь с данной электронной почтой уже зарегистрирован");
            }
        }
    }
}
