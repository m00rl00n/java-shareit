package shareit.Booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoReturned;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingDtoReturnedTest {

    @Test
    public void testNoArgsConstructor() {
        BookingDtoReturned bookingDtoReturned = new BookingDtoReturned();
        assertNull(bookingDtoReturned.getId());
        assertNull(bookingDtoReturned.getStart());
        assertNull(bookingDtoReturned.getEnd());
        assertNull(bookingDtoReturned.getItem());
        assertNull(bookingDtoReturned.getBooker());
        assertNull(bookingDtoReturned.getStatus());
    }

    @Test
    public void testAllArgsConstructor() {
        Integer id = 1;
        LocalDateTime start = LocalDateTime.of(2023, 8, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 8, 1, 14, 0);
        User booker = new User(1, "Маша", "masha@yandex.ru");
        Item item = new Item(2, "Книга", "Описание книги",
                true, booker, new ItemRequest());
        Status status = Status.WAITING;

        BookingDtoReturned bookingDtoReturned = new BookingDtoReturned(id, start, end, item, booker, status);

        assertEquals(id, bookingDtoReturned.getId());
        assertEquals(start, bookingDtoReturned.getStart());
        assertEquals(end, bookingDtoReturned.getEnd());
        assertEquals(item, bookingDtoReturned.getItem());
        assertEquals(booker, bookingDtoReturned.getBooker());
        assertEquals(status, bookingDtoReturned.getStatus());
    }

    @Test
    public void testEqualsAndHashCode() {
        Integer id = 1;
        LocalDateTime start = LocalDateTime.of(2023, 8, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 8, 1, 14, 0);
        User booker = new User(1, "Маша", "masha@yandex.ru");
        Item item = new Item(2, "Книга", "Описание книги",
                true, booker, new ItemRequest());
        Status status = Status.APPROVED;

        BookingDtoReturned bookingDtoReturned1 = new BookingDtoReturned(id, start, end, item, booker, status);
        BookingDtoReturned bookingDtoReturned2 = new BookingDtoReturned(id, start, end, item, booker, status);
        BookingDtoReturned bookingDtoReturned3 = new BookingDtoReturned(2, start.plusDays(1), end.plusDays(1), item,
                booker, status);

        assertTrue(bookingDtoReturned1.equals(bookingDtoReturned2));
        assertFalse(bookingDtoReturned1.equals(bookingDtoReturned3));

        assertEquals(bookingDtoReturned1.hashCode(), bookingDtoReturned2.hashCode());
        assertNotEquals(bookingDtoReturned1.hashCode(), bookingDtoReturned3.hashCode());
    }
}
