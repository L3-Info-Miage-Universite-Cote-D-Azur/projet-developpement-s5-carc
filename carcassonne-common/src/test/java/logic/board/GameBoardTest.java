package logic.board;

import logic.Game;
import logic.TestUtils;
import logic.math.Vector2;
import logic.tile.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    @Test
    void testPlace() {
        Game game = TestUtils.initGameEnv(2, false, true);
        GameBoard gameBoard = new GameBoard();
        assertTrue(gameBoard.isEmpty());

        Tile tile = game.getStack().peek();
        tile.setPosition(new Vector2(0, 0));

        gameBoard.place(tile);

        assertFalse(gameBoard.isEmpty());
        assertTrue(gameBoard.hasTileAt(tile.getPosition()));
        assertEquals(tile, gameBoard.getTileAt(tile.getPosition()));
    }

    @Test
    void testPlaceThrowIfOverlap() {
        Game game = TestUtils.initGameEnv(2, false, true);
        GameBoard gameBoard = new GameBoard();
        assertTrue(gameBoard.isEmpty());

        Vector2 overlapPosition = new Vector2(0, 0);
        Tile tile = game.getStack().peek();
        tile.setPosition(overlapPosition);
        gameBoard.place(tile);

        Tile tile2 = game.getStack().peek();
        tile2.setPosition(overlapPosition);

        assertThrows(IllegalArgumentException.class, () -> {
            gameBoard.place(tile2);
        });
    }

    @Test
    void testClear() {
        Game game = TestUtils.initGameEnv(2, true, true);
        game.getBoard().clear();
        assertTrue(game.getBoard().isEmpty());
    }
}
