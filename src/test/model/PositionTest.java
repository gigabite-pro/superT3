package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PositionTest {
    @Test
    void testEqualPositions() {
        Position pos = new Position(2,3);
        assertTrue(pos.equals(new Position(2,3)));
    }

    @Test
    void testPositionToString() {
        Position pos = new Position(2,3);
        assertEquals(pos.toString(), "2, 3");
    }
}
