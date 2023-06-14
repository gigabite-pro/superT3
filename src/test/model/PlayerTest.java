package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    @Test
    void testCreatePlayer() {
        Player p1 = new Player("X");
        assertEquals(p1.getSymbol(), "X");
    }
}
