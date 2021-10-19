package logic.board;

import logic.math.Vector2;
import logic.tile.RoadTile;
import logic.tile.TileData;
import logic.tile.TileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    @Test
    void testPlace() {
        GameBoard gameBoard = new GameBoard();
        assertTrue(gameBoard.isEmpty());
        Vector2 vector2 = new Vector2(10, 15);
        RoadTile tile0 = new RoadTile(new TileData(TileType.ROAD), vector2);
        gameBoard.place(tile0);
        assertFalse(gameBoard.isEmpty());
        RoadTile tile1 = new RoadTile(new TileData(TileType.RIVER), vector2);
        assertEquals(tile0, gameBoard.getTileAt(vector2));
        assertTrue(gameBoard.hasTileAt(vector2));
        assertEquals(1, gameBoard.getTiles().size()); // TODO UNSAFE
        Vector2 vector0 = new Vector2(35, 15);
        assertFalse(gameBoard.hasTileAt(vector0));
        assertThrows(IllegalArgumentException.class, () -> {
            gameBoard.place(tile1);
        });
    }
}
