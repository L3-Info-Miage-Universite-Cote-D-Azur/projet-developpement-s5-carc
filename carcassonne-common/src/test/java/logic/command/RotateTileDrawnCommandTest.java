package logic.command;


import logic.Game;
import logic.TestUtils;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.Tile;
import logic.tile.TileRotation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RotateTileDrawnCommandTest {
    @Test
    void testRotate() {
        Game game = TestUtils.initGameEnv(5, false, true);
        GameTurnPlaceTileState placeTileState = (GameTurnPlaceTileState) game.getState();
        Tile tileDrawn = placeTileState.getTileDrawn();

        assertFalse(game.executeCommand(new RotateTileDrawnCommand(TileRotation.UP)));
        assertEquals(TileRotation.UP, tileDrawn.getRotation());
        assertTrue(game.executeCommand(new RotateTileDrawnCommand(TileRotation.RIGHT)));
        assertEquals(TileRotation.RIGHT, tileDrawn.getRotation());
        assertTrue(game.executeCommand(new RotateTileDrawnCommand(TileRotation.LEFT)));
        assertEquals(TileRotation.LEFT, tileDrawn.getRotation());
        assertTrue(game.executeCommand(new RotateTileDrawnCommand(TileRotation.DOWN)));
        assertEquals(TileRotation.DOWN, tileDrawn.getRotation());
    }
}
