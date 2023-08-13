package shareit.exception;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.Response;

import static org.junit.jupiter.api.Assertions.*;

public class ResponseTest {

    @Test
    void testGetError() {
        Response response = new Response("ошибка");
        assertEquals("ошибка", response.getError());
    }

    @Test
    void testSetError() {
        Response response = new Response("");
        response.setError("ошибка1");
        assertEquals("ошибка1", response.getError());
    }

    @Test
    void testEmptyError() {
        Response response = new Response("");
        assertTrue(response.getError().isEmpty());
    }

    @Test
    void testNullError() {
        Response response = new Response(null);
        assertNull(response.getError());
    }
}
