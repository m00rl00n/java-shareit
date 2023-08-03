package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {

    @Test
    public void createCommentWithArgsConstructor() {
        Integer id = 1;
        String text = "комментарий";
        Item item = new Item();
        User author = new User();

        Comment comment = new Comment(id, text, item, author);

        assertNotNull(comment);
        assertEquals(id, comment.getId());
        assertEquals(text, comment.getText());
        assertEquals(item, comment.getItem());
        assertEquals(author, comment.getAuthor());
    }

    @Test
    public void createCommentWithNoArgsConstructor() {
        Comment comment = new Comment();

        assertNotNull(comment);
        assertNull(comment.getId());
        assertNull(comment.getText());
        assertNull(comment.getItem());
        assertNull(comment.getAuthor());
    }

    @Test
    public void equalsAndHashCode() {
        Integer id1 = 1;
        String text1 = "комментарий";
        Item item1 = new Item();
        User author1 = new User();
        Comment comment1 = new Comment(id1, text1, item1, author1);

        Integer id2 = 1;
        String text2 = "комментарий";
        Item item2 = new Item();
        User author2 = new User();
        Comment comment2 = new Comment(id2, text2, item2, author2);

        assertEquals(comment1, comment2);
        assertEquals(comment1.hashCode(), comment2.hashCode());
    }

    @Test
    public void notEqualsAndHashCode() {
        Integer id1 = 1;
        String text1 = "комментарий";
        Item item1 = new Item();
        User author1 = new User();
        Comment comment1 = new Comment(id1, text1, item1, author1);

        Integer id2 = 2;
        String text2 = "комментарий";
        Item item2 = new Item();
        User author2 = new User();
        Comment comment2 = new Comment(id2, text2, item2, author2);

        assertNotEquals(comment1, comment2);
        assertNotEquals(comment1.hashCode(), comment2.hashCode());
    }
}
