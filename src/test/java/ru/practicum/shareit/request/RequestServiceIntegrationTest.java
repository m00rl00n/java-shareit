package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.RequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class RequestServiceIntegrationTest {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RequestService requestService;
    @Autowired
    private ItemServiceImpl itemService;

    private final User user = new User(null, "masha@yandex.ru","Маша");
    private final User user2 = new User(null, "masha1@yandex.ru","Маша1");
    private final ItemRequest request = new ItemRequest(null, "request", user2, LocalDateTime.now());


    @Test
    void createRequestTest() {
        UserDto userDto = userService.add(UserDtoMapper.toDto(user));
        user.setId(userDto.getId());

        ItemRequestDto requestDto =
                requestService.create(RequestDtoMapper.toItemRequestDto(request), user.getId());
        request.setId(requestDto.getId());

        assertEquals(requestDto.getId(), request.getId());
        assertEquals(requestDto.getDescription(), request.getDescription());
        assertEquals(requestDto.getRequestorId(), user.getId());
    }

    @Test
    void createRequestWrongUserTest() {
        assertThrows(NotFoundException.class,
                () -> requestService.create(RequestDtoMapper.toItemRequestDto(request), 5));
    }

    @Test
    void createRequestEmptyTextTest() {
        UserDto userDto = userService.add(UserDtoMapper.toDto(user));
        user.setId(userDto.getId());

        request.setDescription("");

        assertThrows(ValidationException.class,
                () -> requestService.create(RequestDtoMapper.toItemRequestDto(request), user.getId()));
    }

    @Test
    void getWrongUserRequestsTest() {
        assertThrows(NotFoundException.class,
                () -> requestService.getUserRequests(5));
    }

    @Test
    void getUserRequestsTest() {
        UserDto userDto = userService.add(UserDtoMapper.toDto(user));
        user.setId(userDto.getId());

        ItemRequestDto requestDto =
                requestService.create(RequestDtoMapper.toItemRequestDto(request), user.getId());
        request.setId(requestDto.getId());

        ItemRequest request1 = new ItemRequest(null, "request1", user2, LocalDateTime.now());
        requestService.create(RequestDtoMapper.toItemRequestDto(request1), user.getId());

        ItemRequest request2 = new ItemRequest(null, "request2", user2, LocalDateTime.now());
        requestService.create(RequestDtoMapper.toItemRequestDto(request2), user.getId());

        List<ItemRequestDto> requests = requestService.getUserRequests(user.getId());

        assertEquals(requests.size(), 3);
        assertEquals(requests.get(0).getDescription(), request.getDescription());
        assertEquals(requests.get(1).getDescription(), request1.getDescription());
        assertEquals(requests.get(2).getDescription(), request2.getDescription());
    }

    @Test
    void getAllRequestsWrongUserTest() {
        assertThrows(NotFoundException.class,
                () -> requestService.getAllRequests(5, 0, 10));
    }

    @Test
    void getAllRequestsTest() {
        UserDto userDto = userService.add(UserDtoMapper.toDto(user));
        user.setId(userDto.getId());
        UserDto userDto1 = userService.add(UserDtoMapper.toDto(user2));
        user2.setId(userDto1.getId());
        ItemRequestDto requestDto =
                requestService.create(RequestDtoMapper.toItemRequestDto(request), user.getId());
        request.setId(requestDto.getId());
        ItemRequest request1 = new ItemRequest(null, "request1", user2, LocalDateTime.now());
        requestService.create(RequestDtoMapper.toItemRequestDto(request1), user2.getId());
        ItemRequest request2 = new ItemRequest(null, "request2", user2, LocalDateTime.now());
        requestService.create(RequestDtoMapper.toItemRequestDto(request2), user.getId());
        List<ItemRequestDto> requests = requestService.getAllRequests(user2.getId(), 0, 10);

        assertEquals(requests.size(), 2);
        assertEquals(requests.get(0).getDescription(), request.getDescription());
        assertEquals(requests.get(1).getDescription(), request2.getDescription());
    }

    @Test
    void getRequestByIdWrongUserTest() {
        UserDto userDto = userService.add(UserDtoMapper.toDto(user));
        user.setId(userDto.getId());

        ItemRequestDto requestDto =
                requestService.create(RequestDtoMapper.toItemRequestDto(request), user.getId());
        request.setId(requestDto.getId());

        assertThrows(NotFoundException.class,
                () -> requestService.getRequestById(5, request.getId()));
    }

    @Test
    void getRequestByWrongIdTest() {
        UserDto userDto = userService.add(UserDtoMapper.toDto(user));
        user.setId(userDto.getId());

        assertThrows(NotFoundException.class,
                () -> requestService.getRequestById(user.getId(), 5));
    }

    @Test
    void getRequestsByIdTest() {
        UserDto userDto = userService.add(UserDtoMapper.toDto(user));
        user.setId(userDto.getId());
        ItemRequestDto requestDto =
                requestService.create(RequestDtoMapper.toItemRequestDto(request), user.getId());
        request.setId(requestDto.getId());
        requestDto = requestService.getRequestById(user.getId(), request.getId());

        assertEquals(requestDto.getId(), request.getId());
        assertEquals(requestDto.getDescription(), request.getDescription());
        assertEquals(requestDto.getRequestorId(), user.getId());
    }

}