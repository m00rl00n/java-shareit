package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class ItemDtoMapper {
    public static ItemDtoOwner toItemDtoOwner(Item item) {
        ItemDtoOwner itemDtoOwner = new ItemDtoOwner();
        itemDtoOwner.setId(item.getId());
        itemDtoOwner.setName(item.getName());
        itemDtoOwner.setDescription(item.getDescription());
        itemDtoOwner.setAvailable(item.getAvailable());
        itemDtoOwner.setRequest(item.getRequest());
        itemDtoOwner.setLastBooking(null);
        itemDtoOwner.setNextBooking(null);
        return itemDtoOwner;
    }

    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwner(item.getOwner());
        itemDto.setRequest(item.getRequest());
        return itemDto;
    }

    public static Item toItem(ItemDto itemDto, User user) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(user);
        item.setRequest(itemDto.getRequest());
        return item;
    }
}