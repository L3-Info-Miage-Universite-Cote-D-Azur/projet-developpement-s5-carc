package logic.command;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import logic.state.GameStateType;
import logic.tile.chunk.ChunkId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaceMeepleCommandTest {
    @Test
    public void testPlacement() {
        Game game = TestUtils.initGameEnv(5, false, true);

        assertTrue(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.getCommandExecutor().execute(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
    }

    @Test
    public void testPlacementOnOccupiedTile() {
        Game game = TestUtils.initGameEnv(5, false, true);

        assertTrue(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.getCommandExecutor().execute(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));

        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_MOVE_DRAGON);
        TestUtils.assertState(game, GameStateType.TURN_PLACE_TILE);

        assertFalse(game.getCommandExecutor().execute(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
    }

    @Test
    public void testMultiplePlacementsOnSameTurn() {
        Game game = TestUtils.initGameEnv(5, false, true);

        assertTrue(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.getCommandExecutor().execute(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
        assertFalse(game.getCommandExecutor().execute(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
    }
}