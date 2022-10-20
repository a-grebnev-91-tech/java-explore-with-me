package ru.practicum.dto.event;

import org.junit.jupiter.api.Test;

import javax.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {
    private final Location location = new Location();

    @Test
    void test1_shouldSetValidLat() {
        assertThrows(ValidationException.class, () -> location.setLat(-90.1));
        assertThrows(ValidationException.class, () -> location.setLat(90.1));
        assertThrows(ValidationException.class, () -> location.setLat(-180.1));
        assertThrows(ValidationException.class, () -> location.setLat(180.1));
        assertDoesNotThrow(() -> location.setLat(-90));
        assertEquals(-90, location.getLat());
        assertDoesNotThrow(() -> location.setLat(0));
        assertEquals(0, location.getLat());
        assertDoesNotThrow(() -> location.setLat(90));
        assertEquals(90, location.getLat());
        assertDoesNotThrow(() -> location.setLat(10.992));
        assertEquals(10.992, location.getLat());
    }

    @Test
    void test2_shouldSetValidLon() {
        assertThrows(ValidationException.class, () -> location.setLon(-180.1));
        assertThrows(ValidationException.class, () -> location.setLon(180.1));
        assertThrows(ValidationException.class, () -> location.setLon(-270.1));
        assertThrows(ValidationException.class, () -> location.setLon(270.1));
        assertDoesNotThrow(() -> location.setLon(-180));
        assertEquals(-180, location.getLon());
        assertDoesNotThrow(() -> location.setLon(0));
        assertEquals(0, location.getLon());
        assertDoesNotThrow(() -> location.setLon(180));
        assertEquals(180, location.getLon());
        assertDoesNotThrow(() -> location.setLon(10.992));
        assertEquals(10.992, location.getLon());
    }
}