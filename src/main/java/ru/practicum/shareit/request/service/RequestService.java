package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface RequestService {
    ItemRequestDto create(ItemRequestDto itemRequestDto, Integer userId);

    List<ItemRequestDto> getUserRequests(Integer userId);

    List<ItemRequestDto> getAllRequests(Integer userId, Integer from, Integer size);

    ItemRequestDto getRequestById(Integer userId, Integer requestId);
}
