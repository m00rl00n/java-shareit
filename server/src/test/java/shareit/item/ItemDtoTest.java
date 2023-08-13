package shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ItemDtoTest {

    @Test
    void testItemDtoConstructorAndGetters() {
        Integer id = 1;
        String name = "Книга";
        String description = "Описание книги";
        Boolean available = true;
        User owner = new User(1, "masha@example.com", "Маша");
        Integer requestId = 10;
        List<CommentDto> comments = new ArrayList<>();

        ItemDto itemDto = new ItemDto(id, name, description, available, owner, requestId, comments);

        assertEquals(id, itemDto.getId());
        assertEquals(name, itemDto.getName());
        assertEquals(description, itemDto.getDescription());
        assertEquals(available, itemDto.getAvailable());
        assertEquals(owner, itemDto.getOwner());
        assertEquals(requestId, itemDto.getRequestId());
        assertEquals(comments, itemDto.getComments());
    }

    @Test
    void testItemDtoSetters() {
        ItemDto itemDto = new ItemDto();

        Integer id = 1;
        String name = "Книга";
        String description = "Описание книги";
        Boolean available = true;
        User owner = new User(1, "masha@example.com", "Маша");
        Integer requestId = 10;
        List<CommentDto> comments = new ArrayList<>();

        itemDto.setId(id);
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setAvailable(available);
        itemDto.setOwner(owner);
        itemDto.setRequestId(requestId);
        itemDto.setComments(comments);

        assertEquals(id, itemDto.getId());
        assertEquals(name, itemDto.getName());
        assertEquals(description, itemDto.getDescription());
        assertEquals(available, itemDto.getAvailable());
        assertEquals(owner, itemDto.getOwner());
        assertEquals(requestId, itemDto.getRequestId());
        assertEquals(comments, itemDto.getComments());
    }

    @Test
    void testItemDtoEqualsAndHashCode() {
        ItemDto itemDto1 = new ItemDto(1, "Книга", "Описание книги", true, new User(), null, new ArrayList<>());
        ItemDto itemDto2 = new ItemDto(1, "Книга", "Описание книги", true, new User(), null, new ArrayList<>());
        ItemDto itemDto3 = new ItemDto(2, "Другая книга", "Описание другой книги", false, new User(), null, new ArrayList<>());

        assertEquals(itemDto1, itemDto2);
        assertEquals(itemDto1.hashCode(), itemDto2.hashCode());
        assertNotEquals(itemDto1, itemDto3);
        assertNotEquals(itemDto1.hashCode(), itemDto3.hashCode());
    }
}
