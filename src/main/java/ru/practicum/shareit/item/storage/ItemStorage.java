package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    List<Item> getItemsByUserId(Integer userId);

    List<Item> getItems();

    Item updateItem(Integer userId, Integer itemId, Item newItem);

    Item getItemById(Integer itemId);

    Item addItem(Integer userId, Item mapDto);

    List<Item> searchItems(Integer userId, String text);
}