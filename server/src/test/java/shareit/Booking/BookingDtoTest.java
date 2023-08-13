package shareit.Booking;


import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingDtoTest {

    @Test
    public void testNoArgsConstructor() {
        BookingDto bookingDto = new BookingDto();
        assertNull(bookingDto.getId());
        assertNull(bookingDto.getStart());
        assertNull(bookingDto.getEnd());
        assertNull(bookingDto.getItemId());
        assertNull(bookingDto.getBookerId());
        assertNull(bookingDto.getStatus());
    }

    @Test
    public void testAllArgsConstructor() {
        Integer id = 1;
        LocalDateTime start = LocalDateTime.of(2023, 8, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 8, 1, 14, 0);
        Integer itemId = 42;
        Integer bookerId = 100;
        Status status = Status.APPROVED;

        BookingDto bookingDto = new BookingDto(id, start, end, itemId, bookerId, status);

        assertEquals(id, bookingDto.getId());
        assertEquals(start, bookingDto.getStart());
        assertEquals(end, bookingDto.getEnd());
        assertEquals(itemId, bookingDto.getItemId());
        assertEquals(bookerId, bookingDto.getBookerId());
        assertEquals(status, bookingDto.getStatus());
    }

    @Test
    public void testEqualsAndHashCode() {
        Integer id = 1;
        LocalDateTime start = LocalDateTime.of(2023, 8, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 8, 1, 14, 0);
        Integer itemId = 42;
        Integer bookerId = 100;
        Status status = Status.APPROVED;

        BookingDto bookingDto1 = new BookingDto(id, start, end, itemId, bookerId, status);
        BookingDto bookingDto2 = new BookingDto(id, start, end, itemId, bookerId, status);
        BookingDto bookingDto3 = new BookingDto(2, start.plusDays(1), end.plusDays(1), itemId, bookerId, status);

        assertTrue(bookingDto1.equals(bookingDto2));
        assertFalse(bookingDto1.equals(bookingDto3));


        assertEquals(bookingDto1.hashCode(), bookingDto2.hashCode());
        assertNotEquals(bookingDto1.hashCode(), bookingDto3.hashCode());
    }
}