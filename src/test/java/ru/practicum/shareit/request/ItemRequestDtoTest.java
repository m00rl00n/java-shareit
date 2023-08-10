package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDtoOwner;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemRequestDtoTest {

    @Test
    public void testNoArgsConstructor() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        assertNull(itemRequestDto.getId());
        assertNull(itemRequestDto.getDescription());
        assertNull(itemRequestDto.getRequestorId());
        assertNull(itemRequestDto.getCreated());
        assertNull(itemRequestDto.getItems());
    }

    @Test
    public void testAllArgsConstructor() {
        Integer id = 1;
        String description = "описание";
        Integer requestorId = 42;
        LocalDateTime created = LocalDateTime.of(2023, 8, 2, 12, 0);
        List<ItemDtoOwner> items = new ArrayList<>();
        ItemDtoOwner itemDtoOwner = new ItemDtoOwner();
        items.add(itemDtoOwner);

        ItemRequestDto itemRequestDto = new ItemRequestDto(id, description, requestorId, created, items);

        assertEquals(id, itemRequestDto.getId());
        assertEquals(description, itemRequestDto.getDescription());
        assertEquals(requestorId, itemRequestDto.getRequestorId());
        assertEquals(created, itemRequestDto.getCreated());
        assertEquals(items, itemRequestDto.getItems());
    }

    @Test
    public void testEqualsAndHashCode() {
        Integer id = 1;
        String description = "описание";
        Integer requestorId = 42;
        LocalDateTime created = LocalDateTime.of(2023, 8, 2, 12, 0);
        List<ItemDtoOwner> items = new ArrayList<>();
        ItemDtoOwner itemDtoOwner = new ItemDtoOwner();
        items.add(itemDtoOwner);

        ItemRequestDto itemRequestDto1 = new ItemRequestDto(id, description, requestorId, created, items);
        ItemRequestDto itemRequestDto2 = new ItemRequestDto(id, description, requestorId, created, items);
        ItemRequestDto itemRequestDto3 = new ItemRequestDto(2, "описание2", 43,
                created.plusDays(1), new ArrayList<>());


        assertTrue(itemRequestDto1.equals(itemRequestDto2));
        assertFalse(itemRequestDto1.equals(itemRequestDto3));


        assertEquals(itemRequestDto1.hashCode(), itemRequestDto2.hashCode());
        assertNotEquals(itemRequestDto1.hashCode(), itemRequestDto3.hashCode());
    }
}
