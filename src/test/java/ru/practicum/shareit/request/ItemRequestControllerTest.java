package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestServiceImpl;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class ItemRequestControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    RequestServiceImpl requestService;

    ItemRequestDto itemRequestDto;
    List<ItemRequestDto> itemRequestDtos;

    @BeforeEach
    void init() {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1);
        itemRequestDto.setDescription("Описание");
        itemRequestDto.setRequestorId(1);

        itemRequestDtos = List.of(itemRequestDto);
    }

    @Test
    @SneakyThrows
    void createRequestTest_thenReturnOK() {
        when(requestService.create(any(ItemRequestDto.class), anyInt()))
                .thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .content(asJsonString(itemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(itemRequestDto)));

        verify(requestService).create(any(ItemRequestDto.class), anyInt());
    }

    @Test
    @SneakyThrows
    void getUserRequestsTest_thenReturnOK() {
        when(requestService.getUserRequests(anyInt()))
                .thenReturn(itemRequestDtos);

        mvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(itemRequestDtos)));

        verify(requestService).getUserRequests(anyInt());
    }

    @Test
    @SneakyThrows
    void getAllRequestsTest_thenReturnOK() {
        when(requestService.getAllRequests(anyInt(), anyInt(), anyInt()))
                .thenReturn(itemRequestDtos);

        mvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(itemRequestDtos)));

        verify(requestService).getAllRequests(anyInt(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getRequestByIdTest_thenReturnOK() {
        when(requestService.getRequestById(anyInt(), anyInt()))
                .thenReturn(itemRequestDto);

        mvc.perform(get("/requests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(itemRequestDto)));

        verify(requestService).getRequestById(anyInt(), anyInt());
    }

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
