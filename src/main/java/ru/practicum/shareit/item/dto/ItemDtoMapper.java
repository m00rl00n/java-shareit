package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;


public class ItemDtoMapper {
    public static ItemDtoOwner toItemDtoOwner(Item item) {
        Integer requestId = null;
        if (item.getRequest() != null) {
            requestId = item.getRequest().getId();
        }
        ItemDtoOwner itemDtoOwner = new ItemDtoOwner();
        itemDtoOwner.setId(item.getId());
        itemDtoOwner.setName(item.getName());
        itemDtoOwner.setDescription(item.getDescription());
        itemDtoOwner.setAvailable(item.getAvailable());
        itemDtoOwner.setRequestId(requestId);
        itemDtoOwner.setLastBooking(null);
        itemDtoOwner.setNextBooking(null);
        return itemDtoOwner;
    }

    public static ItemDto toItemDto(Item item) {
        Integer requestId = null;
        if (item.getRequest() != null) {
            requestId = item.getRequest().getId();
        }
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwner(item.getOwner());
        itemDto.setRequestId(requestId);
        return itemDto;
    }

    public static Item toItem(ItemDto itemDto, User user, ItemRequest itemRequest) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(user);
        item.setRequest(itemRequest);
        return item;
    }
}