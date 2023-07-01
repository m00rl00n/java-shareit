package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemDtoMapper {
    public ItemDto toDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setOwner(item.getOwner());
        dto.setRequestId(item.getRequestId());
        return dto;
    }

    public Item toEntity(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(itemDto.getOwner());
        item.setRequestId(itemDto.getRequestId());
        return item;
    }

    public List<ItemDto> toDtoList(List<Item> items) {
        return items.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
