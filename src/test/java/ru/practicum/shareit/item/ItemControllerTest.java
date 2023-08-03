package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOwner;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class ItemControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ItemServiceImpl itemService;

    ItemDto itemDto;
    CommentDto commentDto;
    ItemDtoOwner itemDtoOwner;

    @BeforeEach
    void init() {
        itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("Item");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);
        itemDto.setRequestId(1);
        itemDto.setComments(new ArrayList<>());

        commentDto = new CommentDto();
        commentDto.setId(1);
        commentDto.setText("Comment");

        itemDtoOwner = new ItemDtoOwner();
        itemDtoOwner.setId(1);
        itemDtoOwner.setName("Item");
        itemDtoOwner.setDescription("Description");
        itemDtoOwner.setAvailable(true);
        itemDtoOwner.setRequestId(1);
        itemDtoOwner.setLastBooking(null);
        itemDtoOwner.setNextBooking(null);
        itemDtoOwner.setComments(new ArrayList<>());
    }

    @Test
    @SneakyThrows
    void addItemTest_thenReturnOK() {
        when(itemService.add(any(ItemDto.class), anyInt()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(asJsonString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(itemDto)));

        verify(itemService).add(any(ItemDto.class), anyInt());
    }

    @Test
    @SneakyThrows
    void commentItemTest_thenReturnOK() {
        when(itemService.commentItem(anyInt(), any(CommentDto.class), anyInt()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .content(asJsonString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(commentDto)));

        verify(itemService).commentItem(anyInt(), any(CommentDto.class), anyInt());
    }

    @Test
    @SneakyThrows
    void updateItemTest_thenReturnOK() {
        when(itemService.update(anyInt(), any(ItemDto.class), anyInt()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                        .content(asJsonString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(itemDto)));

        verify(itemService).update(anyInt(), any(ItemDto.class), anyInt());
    }

    @Test
    @SneakyThrows
    void getItemTest_thenReturnOK() {
        when(itemService.getById(anyInt(), anyInt()))
                .thenReturn(itemDtoOwner);

        mvc.perform(get("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(itemDtoOwner)));

        verify(itemService).getById(anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getAllItemsTest_thenReturnOK() {
        when(itemService.getItemsByUserId(anyInt(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDtoOwner));

        mvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(List.of(itemDtoOwner))));

        verify(itemService).getItemsByUserId(anyInt(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void searchItemsTest_thenReturnOK() {
        when(itemService.search(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .param("text", "Search text")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(List.of(itemDto))));

        verify(itemService).search(anyString(), anyInt(), anyInt());
    }

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}