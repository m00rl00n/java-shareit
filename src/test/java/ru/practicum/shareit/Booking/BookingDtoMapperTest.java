package ru.practicum.shareit.Booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReceived;
import ru.practicum.shareit.booking.dto.BookingDtoReturned;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
public class BookingDtoMapperTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testToBookingSerialization() throws Exception {
        BookingDtoReceived bookingDtoReceived = new BookingDtoReceived();
        bookingDtoReceived.setId(1);
        bookingDtoReceived.setStart(LocalDateTime.of(2023, 7, 29, 10, 0));
        bookingDtoReceived.setEnd(LocalDateTime.of(2023, 7, 29, 12, 0));
        bookingDtoReceived.setStatus(Status.WAITING);

        User user = new User();
        user.setId(200);
        user.setName("Маша");
        bookingDtoReceived.setBooker(user);

        Item item = new Item();
        item.setId(100);
        item.setName("Книга");
        bookingDtoReceived.setItemId(item.getId());

        Booking booking = BookingDtoMapper.toBooking(bookingDtoReceived, user, item);

        String json = objectMapper.writeValueAsString(booking);

        assertThat(json).isNotEmpty();

        Booking deserializedBooking = objectMapper.readValue(json, Booking.class);

        assertThat(deserializedBooking.getId()).isEqualTo(booking.getId());
        assertThat(deserializedBooking.getStart()).isEqualTo(booking.getStart());
        assertThat(deserializedBooking.getEnd()).isEqualTo(booking.getEnd());
        assertThat(deserializedBooking.getItem()).isEqualTo(booking.getItem());
        assertThat(deserializedBooking.getBooker()).isEqualTo(booking.getBooker());
        assertThat(deserializedBooking.getStatus()).isEqualTo(booking.getStatus());
    }

    @Test
    void testToBookingDtoReturnedDeserialization() throws Exception {
        String json = "{\"id\":1,\"start\":\"2023-07-29T10:00:00\",\"end\":\"2023-07-29T12:00:00\",\"item\":" +
                "{\"id\":100,\"name\":\"Книга\",\"description\":null,\"available\":null,\"owner\":null," +
                "\"request\":null},\"booker\":{\"id\":200,\"email\":null,\"name\":\"Маша\"},\"status\":\"WAITING\"}";

        Booking booking = objectMapper.readValue(json, Booking.class);
        BookingDtoReturned bookingDtoReturned = BookingDtoMapper.toBookingDtoReturned(booking);

        assertThat(bookingDtoReturned.getId()).isEqualTo(1);
        assertThat(bookingDtoReturned.getStart())
                .isEqualTo(LocalDateTime.of(2023, 7, 29, 10, 0));
        assertThat(bookingDtoReturned.getEnd())
                .isEqualTo(LocalDateTime.of(2023, 7, 29, 12, 0));

        User user = new User();
        user.setId(200);
        user.setName("Маша");

        Item item = new Item();
        item.setId(100);
        item.setName("Книга");

        assertThat(bookingDtoReturned.getBooker()).isEqualTo(user);
        assertThat(bookingDtoReturned.getItem()).isEqualTo(item);
        assertThat(bookingDtoReturned.getStatus()).isEqualTo(Status.WAITING);
    }

    @Test
    void testToBookingDtoSerialization() throws Exception {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.of(2023, 7, 29, 10, 0));
        booking.setEnd(LocalDateTime.of(2023, 7, 29, 12, 0));

        User user = new User();
        user.setId(200);
        user.setName("Маша");
        booking.setBooker(user);

        Item item = new Item();
        item.setId(100);
        item.setName("Книга");
        booking.setItem(item);

        booking.setStatus(Status.WAITING);

        BookingDto bookingDto = BookingDtoMapper.toBookingDto(booking);

        String json = objectMapper.writeValueAsString(bookingDto);

        assertThat(json).isNotEmpty();
        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"start\":\"2023-07-29T10:00:00\"");
        assertThat(json).contains("\"end\":\"2023-07-29T12:00:00\"");
        assertThat(json).contains("\"bookerId\":200");
        assertThat(json).contains("\"itemId\":100");
        assertThat(json).contains("\"status\":\"WAITING\"");
    }
}

