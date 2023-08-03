package ru.practicum.shareit.Booking;

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
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoReceived;
import ru.practicum.shareit.booking.dto.BookingDtoReturned;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class BookingControllerTest {
    BookingDtoReceived bookingDtoReceived;
    BookingDtoReturned bookingDtoReturned;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    @MockBean
    BookingServiceImpl bookingService;

    @BeforeEach
    void init() {
        bookingDtoReturned = new BookingDtoReturned();
        bookingDtoReturned.setId(1);
        bookingDtoReturned.setStart(LocalDateTime.of(2023, 8, 1, 12, 0));
        bookingDtoReturned.setEnd(LocalDateTime.of(2023, 8, 1, 14, 0));
    }

    @Test
    @SneakyThrows
    void createBookingTest_thenReturnOK() {
        BookingDtoReceived bookingDtoReceived = new BookingDtoReceived();
        BookingDtoReturned bookingDtoReturned = new BookingDtoReturned();
        Integer userId = 1;

        when(bookingService.create(any(BookingDtoReceived.class), anyInt()))
                .thenReturn(bookingDtoReturned);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoReceived))
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDtoReturned)));

        verify(bookingService).create(any(BookingDtoReceived.class), anyInt());
    }

    @Test
    @SneakyThrows
    void bookingConfirmationTest_thenReturnOK() {
        Integer bookingId = 1;
        Boolean approved = true;
        Integer userId = 1;
        BookingDtoReturned bookingDtoReturned = new BookingDtoReturned();

        when(bookingService.bookingConfirmation(anyInt(), anyBoolean(), anyInt()))
                .thenReturn(bookingDtoReturned);

        mvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDtoReturned)));

        verify(bookingService).bookingConfirmation(anyInt(), anyBoolean(), anyInt());
    }

    @Test
    @SneakyThrows
    void getBookingTest_thenReturnOK() {
        Integer bookingId = 1;
        Integer userId = 1;
        BookingDtoReturned bookingDtoReturned = new BookingDtoReturned();

        when(bookingService.getBookingById(anyInt(), anyInt()))
                .thenReturn(bookingDtoReturned);

        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDtoReturned)));

        verify(bookingService).getBookingById(anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getAllBookingsByUserTest_thenReturnOK() {
        Integer userId = 1;
        String state = "ALL";
        Integer from = 0;
        Integer size = 10;

        List<BookingDtoReturned> bookings = List.of(bookingDtoReturned);

        when(bookingService.getAllBookingsByUser(anyString(), anyInt(), anyInt(), anyInt()))
                .thenReturn(bookings);

        mvc.perform(get("/bookings")
                        .param("state", state)
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookings)));

        verify(bookingService).getAllBookingsByUser(anyString(), anyInt(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getAllBookingsByOwnerTest_thenReturnOK() {
        Integer userId = 1;
        String state = "ALL";
        Integer from = 0;
        Integer size = 10;

        List<BookingDtoReturned> bookings = List.of(bookingDtoReturned);

        when(bookingService.getAllBookingsByOwner(anyString(), anyInt(), anyInt(), anyInt()))
                .thenReturn(bookings);

        mvc.perform(get("/bookings/owner")
                        .param("state", state)
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookings)));

        verify(bookingService).getAllBookingsByOwner(anyString(), anyInt(), anyInt(), anyInt());
    }

}