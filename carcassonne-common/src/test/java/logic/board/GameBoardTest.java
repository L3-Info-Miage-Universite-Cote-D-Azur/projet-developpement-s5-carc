package logic.board;

import logic.config.excel.TileChunkExcelConfig;
import logic.config.excel.TileExcelConfig;
import logic.math.Vector2;
import logic.tile.Tile;
import logic.tile.TileFlags;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    @Test
    void testPlace() {
        GameBoard gameBoard = new GameBoard();
        assertTrue(gameBoard.isEmpty());

        Tile tile = new Tile(new TileExcelConfig(new TileChunkExcelConfig[0], "A", "default", EnumSet.of(TileFlags.STARTING), 1));

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
        gameBoard.place(new Tile(new TileExcelConfig(new TileChunkExcelConfig[0], "A", "default", EnumSet.of(TileFlags.STARTING), 1)) {{
            setPosition(overlapPosition);
        }});

        assertThrows(IllegalArgumentException.class, () -> {
            gameBoard.place(new Tile(new TileExcelConfig(new TileChunkExcelConfig[0], "A", "default", EnumSet.noneOf(TileFlags.class), 1)) {{
                setPosition(overlapPosition);
            }});
        });
    }
}
