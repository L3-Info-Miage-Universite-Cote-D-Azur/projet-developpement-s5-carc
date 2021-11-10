package logic.board;

import logic.config.GameConfig;
import logic.math.Vector2;
import logic.tile.Tile;
import logic.tile.TileFlags;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    @Test
    void testPlace() {
        GameBoard gameBoard = new GameBoard();
        assertTrue(gameBoard.isEmpty());

        Tile tile = new Tile();

        tile.setFlags(TileFlags.STARTING, true);
        tile.setPosition(new Vector2(0, 0));

        gameBoard.place(tile);

        assertFalse(gameBoard.isEmpty());
        assertTrue(gameBoard.hasTileAt(tile.getPosition()));
        assertEquals(tile, gameBoard.getTileAt(tile.getPosition()));
    }

    @Test
    void testPlaceThrowIfOverlap() {
        GameBoard gameBoard = new GameBoard();
        assertTrue(gameBoard.isEmpty());

        Vector2 overlapPosition = new Vector2(0, 0);
        gameBoard.place(new Tile() {{
            setFlags(TileFlags.STARTING, true);
            setPosition(overlapPosition);
        }});

        assertThrows(IllegalArgumentException.class, () -> {
            gameBoard.place(new Tile() {{
                setPosition(overlapPosition);
            }});
        });
    }
}
