package ru.practicum.shareit.Booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    @Test
    void testNoArgsConstructor() {
        Booking booking = new Booking();
        assertNull(booking.getId());
        assertNull(booking.getStart());
        assertNull(booking.getEnd());
        assertNull(booking.getItem());
        assertNull(booking.getBooker());
        assertNull(booking.getStatus());
    }

    @Test
    void testAllArgsConstructor() {
        Integer id = 1;
        LocalDateTime start = LocalDateTime.of(2023, 8, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 8, 1, 14, 0);
        Item item = new Item();
        User booker = new User();
        Status status = Status.APPROVED;

        Booking booking = new Booking(id, start, end, item, booker, status);

        assertEquals(id, booking.getId());
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(item, booking.getItem());
        assertEquals(booker, booking.getBooker());
        assertEquals(status, booking.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        Integer id = 1;
        LocalDateTime start = LocalDateTime.of(2023, 8, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 8, 1, 14, 0);
        Item item = new Item();
        User booker = new User();
        Status status = Status.APPROVED;

        Booking booking1 = new Booking(id, start, end, item, booker, status);
        Booking booking2 = new Booking(id, start, end, item, booker, status);
        Booking booking3 = new Booking(2, start.plusDays(1), end.plusDays(1), item, booker, status);

        assertTrue(booking1.equals(booking2));
        assertFalse(booking1.equals(booking3));

        assertEquals(booking1.hashCode(), booking2.hashCode());
        assertNotEquals(booking1.hashCode(), booking3.hashCode());
    }
}
