package ru.practicum.shareit.Booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.State;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StateTest {

    @Test
    void testAllState() {
        State state = State.ALL;
        assertEquals("ALL", state.name());
        assertEquals(0, state.ordinal());
    }

    @Test
    void testPastState() {
        State state = State.PAST;
        assertEquals("PAST", state.name());
        assertEquals(1, state.ordinal());
    }

    @Test
    void testCurrentState() {
        State state = State.CURRENT;
        assertEquals("CURRENT", state.name());
        assertEquals(2, state.ordinal());
    }

    @Test
    void testFutureState() {
        State state = State.FUTURE;
        assertEquals("FUTURE", state.name());
        assertEquals(3, state.ordinal());
    }

    @Test
    void testWaitingState() {
        State state = State.WAITING;
        assertEquals("WAITING", state.name());
        assertEquals(4, state.ordinal());
    }

    @Test
    void testRejectedState() {
        State state = State.REJECTED;
        assertEquals("REJECTED", state.name());
        assertEquals(5, state.ordinal());
    }
}
