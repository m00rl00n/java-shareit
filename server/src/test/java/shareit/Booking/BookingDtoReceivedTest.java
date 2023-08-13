package shareit.Booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoReceived;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingDtoReceivedTest {

    @Test
    public void testNoArgsConstructor() {
        BookingDtoReceived bookingDtoReceived = new BookingDtoReceived();
        assertNull(bookingDtoReceived.getId());
        assertNull(bookingDtoReceived.getStart());
        assertNull(bookingDtoReceived.getEnd());
        assertNull(bookingDtoReceived.getItemId());
        assertNull(bookingDtoReceived.getBooker());
        assertNull(bookingDtoReceived.getStatus());
    }

    @Test
    public void testAllArgsConstructor() {
        Integer id = 1;
        LocalDateTime start = LocalDateTime.of(2023, 8, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 8, 1, 14, 0);
        Integer itemId = 42;
        User booker = new User(1, "Маша", "masha@yandex.ru");
        Status status = Status.APPROVED;

        BookingDtoReceived bookingDtoReceived = new BookingDtoReceived(id, start, end, itemId, booker, status);

        assertEquals(id, bookingDtoReceived.getId());
        assertEquals(start, bookingDtoReceived.getStart());
        assertEquals(end, bookingDtoReceived.getEnd());
        assertEquals(itemId, bookingDtoReceived.getItemId());
        assertEquals(booker, bookingDtoReceived.getBooker());
        assertEquals(status, bookingDtoReceived.getStatus());
    }

    @Test
    public void testEqualsAndHashCode() {
        Integer id = 1;
        LocalDateTime start = LocalDateTime.of(2023, 8, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 8, 1, 14, 0);
        Integer itemId = 42;
        User booker = new User(1, "Маша", "masha@yandex.ru");
        Status status = Status.APPROVED;

        BookingDtoReceived bookingDtoReceived1 = new BookingDtoReceived(id, start, end, itemId, booker, status);
        BookingDtoReceived bookingDtoReceived2 = new BookingDtoReceived(id, start, end, itemId, booker, status);
        BookingDtoReceived bookingDtoReceived3 = new BookingDtoReceived(2, start.plusDays(1), end.plusDays(1),
                itemId, booker, status);
        assertTrue(bookingDtoReceived1.equals(bookingDtoReceived2));
        assertFalse(bookingDtoReceived1.equals(bookingDtoReceived3));
        assertEquals(bookingDtoReceived1.hashCode(), bookingDtoReceived2.hashCode());
        assertNotEquals(bookingDtoReceived1.hashCode(), bookingDtoReceived3.hashCode());
    }
}

