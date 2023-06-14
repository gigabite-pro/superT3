package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GuiGameTest {

    GuiGame game;

    @BeforeEach
    void setup() {
        game = new GuiGame();
    }

    @Test
    void getActiveGridTest() {
        int result = game.getActiveGrid();
        assertEquals(result, 0);
    }

    @Test
    void setActiveGridTest() {
        game.setActiveGrid(5);
        Assertions.assertEquals(game.getActiveGrid(), 5);
    }

    @Test
    void isHasGameStartedTest() {
        game.play();
        assertTrue(game.isHasGameStarted());
    }

    @Test
    void addPositionTest() {
        game.play();
        game.setActiveGrid(5);
        game.addPosition(6);
        game.setActiveGrid(5);
        assertEquals(game.getGrid().getSmallGrid(game.getActiveGrid()).getSmallGrid().size(), 1);
    }

    @Test
    void addPositionWonTest() {
        game.play();
        game.setActiveGrid(5);
        game.addPosition(1);
        game.setActiveGrid(5);
        game.addPosition(4);
        game.setActiveGrid(5);
        game.addPosition(2);
        game.setActiveGrid(5);
        game.addPosition(5);
        game.setActiveGrid(5);
        game.addPosition(3);
        game.setActiveGrid(5);
        assertTrue(game.getGrid().getSmallGrid(game.getActiveGrid()).hasSmallWon());
    }

    @Test
    void addPositionTieTest() {
        game.play();
        for (int i = 0; i < 9; i++) {
            game.setActiveGrid(5);
            game.addPosition(i+1);
        }
        game.setActiveGrid(5);
        assertEquals(game.getWonGrids().size(), 2);
    }

    @Test
    void playTest() {
        game.play();
        assertTrue(game.isHasGameStarted());
    }
}
