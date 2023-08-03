package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.dto.CommentDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
public class CommentDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSerializeAndDeserialize() throws Exception {
        Integer id = 1;
        String text = "Это комментарий";
        String item = "Книга";
        String authorName = "Маша";
        LocalDateTime created = LocalDateTime.now();

        CommentDto commentDto = new CommentDto();
        commentDto.setId(id);
        commentDto.setText(text);
        commentDto.setItem(item);
        commentDto.setAuthorName(authorName);
        commentDto.setCreated(created);

        String json = objectMapper.writeValueAsString(commentDto);
        assertNotNull(json);

        CommentDto deserializedCommentDto = objectMapper.readValue(json, CommentDto.class);

        assertEquals(commentDto, deserializedCommentDto);
    }

    @Test
    public void testEqualsAndHashCode() {
        Integer id1 = 1;
        String text1 = "Это комментарий";
        String item1 = "Книга";
        String authorName1 = "Маша";
        LocalDateTime created1 = LocalDateTime.now().withNano(0);
        CommentDto commentDto1 = new CommentDto(id1, text1, item1, authorName1, created1);

        Integer id2 = 1;
        String text2 = "Это комментарий";
        String item2 = "Книга";
        String authorName2 = "Маша";
        LocalDateTime created2 = LocalDateTime.now().withNano(0);
        CommentDto commentDto2 = new CommentDto(id2, text2, item2, authorName2, created2);

        assertEquals(commentDto1, commentDto2);
        assertEquals(commentDto1.hashCode(), commentDto2.hashCode());
    }

    @Test
    public void testNotEqualsAndHashCode() {
        Integer id1 = 1;
        String text1 = "Это комментарий";
        String item1 = "Книга";
        String authorName1 = "Маша";
        LocalDateTime created1 = LocalDateTime.now().withNano(0);
        CommentDto commentDto1 = new CommentDto(id1, text1, item1, authorName1, created1);

        Integer id2 = 2;
        String text2 = "Это другой комментарий";
        String item2 = "Статья";
        String authorName2 = "Петя";
        LocalDateTime created2 = LocalDateTime.now().withNano(0).minusDays(1);
        CommentDto commentDto2 = new CommentDto(id2, text2, item2, authorName2, created2);

        assertNotEquals(commentDto1, commentDto2);
        assertNotEquals(commentDto1.hashCode(), commentDto2.hashCode());
    }
}
