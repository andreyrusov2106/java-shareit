package ru.practicum.shareit.unit.state;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.exceptions.IllegalEnumStateException;

public class StateTest {
    @Test
    public void testStringToStateCurrent() {
        Assertions.assertEquals(State.stringToState("CURRENT"), State.CURRENT);
    }

    @Test
    public void testStringToStateNull() {
        Assertions.assertEquals(State.stringToState(null), State.ALL);
    }

    @Test
    public void testStringToStateAll() {
        final IllegalEnumStateException exception = Assertions.assertThrows(
                IllegalEnumStateException.class,
                () -> State.stringToState(""));

        Assertions.assertEquals("Unknown state: ", exception.getMessage());
    }

}
