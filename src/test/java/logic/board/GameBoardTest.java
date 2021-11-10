package logic.board;

import logic.config.GameConfig;
import logic.config.TileData;
import logic.math.Vector2;
import logic.tile.Tile;
import logic.tile.TileFlags;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    @Test
    void testPlace() {
        GameBoard gameBoard = new GameBoard();
        assertTrue(gameBoard.isEmpty());

        Tile tile = new Tile(new TileData("A", 1, "default", EnumSet.of(TileFlags.STARTING)));

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
        gameBoard.place(new Tile(new TileData("A", 1, "default", EnumSet.of(TileFlags.STARTING))) {{
            setPosition(overlapPosition);
        }});

        assertThrows(IllegalArgumentException.class, () -> {
            gameBoard.place(new Tile(new TileData("A", 1, "default", EnumSet.noneOf(TileFlags.class))) {{
                setPosition(overlapPosition);
            }});
        });
    }
}
