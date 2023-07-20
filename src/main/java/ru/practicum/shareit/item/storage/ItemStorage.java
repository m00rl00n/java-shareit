package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    List<Item> getItemsByUserId(Integer userId);

    List<Item> getItems();

    Item update(Integer userId, Integer itemId, Item newItem);

    Item getItemById(Integer itemId);

    Item add(Integer userId, Item mapDto);

    List<Item> search(Integer userId, String text);
}