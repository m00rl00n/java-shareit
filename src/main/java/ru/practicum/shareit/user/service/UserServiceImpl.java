package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;


import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final UserDtoMapper userDtoMapper;
    static final String EMAIL_PATTERN = "\\w[\\w.-]*@\\w[\\w.-]*\\w{2,4}";


    @Autowired
    public UserServiceImpl( UserRepository userRepository, UserDtoMapper userDtoMapper) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Получение всех пользователей");
        List<User> users = userRepository.findAll();
        return userDtoMapper.toDtoList(users);
    }

    @Override
    public UserDto getUser(Integer id) {
        log.info("Получение пользователя с id=" + id);
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        return userDtoMapper.toDto(user);

    }

    @Override
    public UserDto add(UserDto user) {
        log.info("Добавление нового пользователя");
        User userNew = userDtoMapper.toEntity(user);
        validateUserEmail(userNew.getEmail());
        return userDtoMapper.toDto(userRepository.save(userNew));
    }

    @Override
    public UserDto update(UserDto userDto, Integer id) {
        log.info("Обновление информации о пользователе id=" + id);
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            validateUserEmail(userDto.getEmail());
            user.setEmail(userDto.getEmail());
        }
        return userDtoMapper.toDto(userRepository.save(user));
    }

    @Override
    public void remove(Integer id) {
        log.info("Удаление пользователя id=" + id);
        userDtoMapper.toEntity(getUser(id));
        userRepository.deleteById(id);
    }

    private void validateUserEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email не может быть пустым");
        }
        if (!email.matches(EMAIL_PATTERN)) {
            throw new ValidationException("Проверьте корректность почты");
        }
    }


}
