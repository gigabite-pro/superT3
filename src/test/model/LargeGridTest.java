package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LargeGridTest {
    LargeGrid lg;

    @BeforeEach
    void setup() {
        lg = new LargeGrid();
    }

    @Test
    void testGetGrid() {
        assertEquals(lg.getGrid().size(), 9);
    }

    @Test
    void testRowAligned() {
        lg.getSmallGrid(1).makeWin("X");
        lg.getSmallGrid(2).makeWin("X");
        lg.getSmallGrid(3).makeWin("X");
        assertTrue(lg.isAligned("X"));
    }

    @Test
    void testColumnAligned() {
        lg.getSmallGrid(1).makeWin("X");
        lg.getSmallGrid(4).makeWin("X");
        lg.getSmallGrid(7).makeWin("X");
        assertTrue(lg.isAligned("X"));
    }

    @Test
    void testLeftDiagonalAligned() {
        lg.getSmallGrid(1).makeWin("X");
        lg.getSmallGrid(5).makeWin("X");
        lg.getSmallGrid(9).makeWin("X");
        assertTrue(lg.isAligned("X"));
    }

    @Test
    void testRightDiagonalAligned() {
        lg.getSmallGrid(3).makeWin("X");
        lg.getSmallGrid(5).makeWin("X");
        lg.getSmallGrid(7).makeWin("X");
        assertTrue(lg.isAligned("X"));
    }

    @Test
    void testToString() {
        System.out.println(lg.toString());
    }
}
