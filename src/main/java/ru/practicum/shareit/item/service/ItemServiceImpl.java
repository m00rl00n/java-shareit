package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemServiceImpl implements ItemService {

    final ItemStorage itemStorage;
    final ItemDtoMapper itemDtoMapper;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, ItemDtoMapper itemDtoMapper) {
        this.itemStorage = itemStorage;
        this.itemDtoMapper = itemDtoMapper;
    }

    public List<ItemDto> getItemsByUserId(Integer id) {
        List<Item> items = itemStorage.getItemsByUserId(id);
        log.info("Получение вещей пользователя");
        return itemDtoMapper.toDtoList(items);
    }

    public ItemDto addItem(Integer id, ItemDto itemDto) {
        log.info("Добавление вещи");
        return itemDtoMapper.toDto(itemStorage.addItem(id, itemDtoMapper.toEntity(itemDto)));
    }

    public ItemDto getItemById(Integer id) {
        Item item = itemStorage.getItemById(id);
        log.info("Получение вещи");
        return itemDtoMapper.toDto(item);
    }

    public List<ItemDto> searchItems(Integer id, String text) {
        List<Item> items = itemStorage.searchItems(id, text);
        List<ItemDto> itemsDto = itemDtoMapper.toDtoList(items);
        log.info("Поиск вещи");
        return itemsDto;
    }

    public ItemDto updateItem(Integer userId, Integer itemId, ItemDto newItemDto) {
        Item updatedItem = itemDtoMapper.toEntity(newItemDto);
        Item item = itemStorage.updateItem(userId, itemId, updatedItem);
        log.info("Обновление вещи");
        return itemDtoMapper.toDto(item);
    }

}