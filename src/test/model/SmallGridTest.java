package model;

import static org.junit.jupiter.api.Assertions.*;
import model.SmallGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

public class SmallGridTest {

    SmallGrid sg;

    @BeforeEach
    void setup() {
        sg = new SmallGrid();
    }

    @Test
    void testWinX() {
        sg.makeWin("X");
        System.out.println(sg.toString());
    }

    @Test
    void testWinO() {
        sg.makeWin("O");
        System.out.println(sg.toString());
    }

    @Test
    void testTie() {
        sg.makeTie();
        System.out.println(sg.toString());
    }

    @Test
    void testNotWonOrTied() {
        sg.add("X", 0,0);
        sg.add("O", 2,1);
        System.out.println(sg.toString());
    }

    @Test
    void testLoadGrid() {
        LinkedHashMap<Integer, String> positions = new LinkedHashMap<>();
        positions.put(5,"X");
        positions.put(2,"O");
        sg.loadSmallGrid(positions);
        System.out.println(sg.toString());
    }

    @Test
    void testRowAligned() {
        sg.add("X", 0,0);
        sg.add("X", 0,1);
        sg.add("X", 0,2);
        assertTrue(sg.isAligned("X"));
    }

    @Test
    void testColumnAligned() {
        sg.add("X", 0,0);
        sg.add("X", 1,0);
        sg.add("X", 2,0);
        assertTrue(sg.isAligned("X"));
    }

    @Test
    void testLeftDiagonalAligned() {
        sg.add("X", 0,0);
        sg.add("X", 1,1);
        sg.add("X", 2,2);
        assertTrue(sg.isAligned("X"));
    }

    @Test
    void testRightDiagonalAligned() {
        sg.add("X", 0,2);
        sg.add("X", 1,1);
        sg.add("X", 2,0);
        assertTrue(sg.isAligned("X"));
    }

    @Test
    void testHasTied() {
        sg.makeTie();
        assertTrue(sg.hasTied());
    }

    @Test
    void testFalseAdd() {
        sg.add("X",2,1);
        assertFalse(sg.add("X",2,1));
    }

    @Test
    void testGetSmallGrid() {
        sg.add("X",2,1);
        assertEquals(sg.getSmallGrid().size(), 1);
    }
}