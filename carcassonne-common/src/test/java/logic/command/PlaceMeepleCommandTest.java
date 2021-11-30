package logic.command;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import logic.tile.chunk.ChunkId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaceMeepleCommandTest {
    @Test
    public void testPlacement() {
        Game game = TestUtils.initGameEnv(5, false, true);

        assertTrue(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.getCommandExecutor().execute(new PlaceMeepleCommand(ChunkId.CENTER_MIDDLE)));
        assertTrue(game.getCommandExecutor().execute(new EndTurnCommand()));
    }

    @Test
    public void testPlacementOnOccupiedTile() {
        Game game = TestUtils.initGameEnv(5, false, true);

        assertTrue(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.getCommandExecutor().execute(new PlaceMeepleCommand(ChunkId.CENTER_MIDDLE)));
        assertTrue(game.getCommandExecutor().execute(new EndTurnCommand()));

        assertFalse(game.isOver());

        game.getState().complete();
        assertFalse(game.getCommandExecutor().execute(new PlaceMeepleCommand(ChunkId.CENTER_MIDDLE)));
    }

    @Test
    public void testMultiplePlacementsOnSameTurn() {
        Game game = TestUtils.initGameEnv(5, false, true);

        assertTrue(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.getCommandExecutor().execute(new PlaceMeepleCommand(ChunkId.CENTER_MIDDLE)));
        assertFalse(game.getCommandExecutor().execute(new PlaceMeepleCommand(ChunkId.CENTER_MIDDLE)));
        assertTrue(game.getCommandExecutor().execute(new EndTurnCommand()));
    }
}