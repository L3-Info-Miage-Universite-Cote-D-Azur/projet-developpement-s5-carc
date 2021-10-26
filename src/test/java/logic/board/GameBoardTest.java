package logic.board;

import logic.math.Vector2;
import logic.tile.RoadTile;
import logic.tile.StartingTile;
import logic.tile.TileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    @Test
    void testPlace() {
        GameBoard gameBoard = new GameBoard();
        assertTrue(gameBoard.isEmpty());

        StartingTile tile = new StartingTile();
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
        gameBoard.place(new StartingTile() {{
            setPosition(overlapPosition);
        }});

        assertThrows(IllegalArgumentException.class, () -> {
            gameBoard.place(new RoadTile() {{
                setPosition(overlapPosition);
            }});
        });
    }
}
