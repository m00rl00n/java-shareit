package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOwner;

import java.util.List;


public interface ItemService {
    ItemDtoOwner getById(Integer itemId, Integer userId);

    ItemDto add(ItemDto itemDto, Integer userId);

    ItemDto update(Integer itemId, ItemDto itemDto, Integer userId);

    CommentDto commentItem(Integer itemId, CommentDto comment, Integer authorId);

    List<ItemDtoOwner> getItemsByUserId(Integer userId);

    List<ItemDto> search(String text);
}
