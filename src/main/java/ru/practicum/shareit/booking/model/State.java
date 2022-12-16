package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exceptions.IllegalEnumStateException;

public enum State {
    WAITING, REJECTED, ALL, CURRENT, PAST, FUTURE;

    public static State stringToState(String stringState) {
        State state;
        try {
            if (stringState == null) {
                state = State.ALL;
            } else {
                state = State.valueOf(stringState);
            }
        } catch (Exception e) {
            throw new IllegalEnumStateException("Unknown state: " + stringState);
        }
        return state;
    }
}
