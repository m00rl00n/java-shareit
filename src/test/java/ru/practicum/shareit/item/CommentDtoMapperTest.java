package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class CommentDtoMapperTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testToCommentDto() {
        Integer commentId = 1;
        String commentText = "Это комментарий";
        String authorName = "Маша";
        String itemName = "Книга";

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setText(commentText);

        User user = new User();
        user.setName(authorName);
        comment.setAuthor(user);

        Item item = new Item();
        item.setName(itemName);
        comment.setItem(item);

        CommentDto commentDto = CommentDtoMapper.toCommentDto(comment);

        assertEquals(comment.getId(), commentDto.getId());
        assertEquals(comment.getText(), commentDto.getText());
        assertEquals(comment.getAuthor().getName(), commentDto.getAuthorName());
        assertEquals(comment.getItem().getName(), commentDto.getItem());
        assertEquals(commentDto.getCreated().withNano(0), LocalDateTime.now().withNano(0));
    }

    @Test
    public void testToComment() {
        Integer commentId = 1;
        String commentText = "Это комментарий";
        String authorName = "Маша";
        String itemName = "Книга";

        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentId);
        commentDto.setText(commentText);
        commentDto.setAuthorName(authorName);
        commentDto.setItem(itemName);
        commentDto.setCreated(LocalDateTime.now());

        User user = new User();
        user.setName(authorName);

        Item item = new Item();
        item.setName(itemName);

        Comment comment = CommentDtoMapper.toComment(commentDto, user, item);

        assertEquals(commentDto.getId(), comment.getId());
        assertEquals(commentDto.getText(), comment.getText());
        assertEquals(commentDto.getAuthorName(), comment.getAuthor().getName());
        assertEquals(commentDto.getItem(), comment.getItem().getName());
    }
}