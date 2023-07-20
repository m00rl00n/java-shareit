package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItemsByUserId(Integer userId);

    ItemDto add(Integer userId, ItemDto itemDto);

    ItemDto getItemById(Integer itemId);

    List<ItemDto> search(Integer userId, String text);

    ItemDto update(Integer userId, Integer itemId, ItemDto newItemDto);
}
