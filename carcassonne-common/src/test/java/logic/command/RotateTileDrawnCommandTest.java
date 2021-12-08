package logic.command;


import logic.Game;
import logic.TestUtils;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.Tile;
import logic.tile.TileRotation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RotateTileDrawnCommandTest {
    @Test
    void testRotate() {
        Game game = TestUtils.initGameEnv(5, false, true);
        GameTurnPlaceTileState placeTileState = (GameTurnPlaceTileState) game.getState();
        Tile tileDrawn = placeTileState.getTileDrawn();

        game.executeCommand(new RotateTileDrawnCommand(TileRotation.UP));
        assertEquals(tileDrawn.getRotation(), TileRotation.UP);
        game.executeCommand(new RotateTileDrawnCommand(TileRotation.RIGHT));
        assertEquals(tileDrawn.getRotation(), TileRotation.RIGHT);
        game.executeCommand(new RotateTileDrawnCommand(TileRotation.LEFT));
        assertEquals(tileDrawn.getRotation(), TileRotation.LEFT);
        game.executeCommand(new RotateTileDrawnCommand(TileRotation.DOWN));
        assertEquals(tileDrawn.getRotation(), TileRotation.DOWN);
    }
}
