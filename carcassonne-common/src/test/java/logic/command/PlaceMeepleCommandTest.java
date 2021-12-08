package logic.command;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import logic.state.GameStateType;
import logic.state.turn.GameTurnPlaceMeepleState;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.chunk.ChunkId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaceMeepleCommandTest {
    @Test
    public void testPlacement() {
        Game game = TestUtils.initGameEnv(5, false, true);

        assertTrue(game.executeCommand(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.executeCommand(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
    }

    @Test
    public void testPlacementOnOccupiedTile() {
        Game game = TestUtils.initGameEnv(5, false, true);

        assertTrue(game.executeCommand(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.executeCommand(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));

        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_MOVE_DRAGON);
        TestUtils.assertState(game, GameStateType.TURN_PLACE_TILE);
        TestUtils.placeTileRandomly(game);
        TestUtils.assertState(game, GameStateType.TURN_PLACE_MEEPLE);

        assertFalse(game.executeCommand(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
    }

    @Test
    public void testPlacementOnOtherTileEvenWithoutPortal() {
        Game game = TestUtils.initGameEnv(5, false, true);

        TestUtils.placeTileRandomly(game);
        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_PLACE_MEEPLE);
        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_MOVE_DRAGON);

        TestUtils.placeTileRandomly(game);

        GameTurnPlaceMeepleState placeMeepleState = (GameTurnPlaceMeepleState) game.getState();

        assertFalse(game.executeCommand(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
        assertTrue(game.executeCommand(new PlaceMeepleCommand(placeMeepleState.getTileDrawnPosition(), ChunkId.CENTER_MIDDLE)));
    }

    @Test
    public void testPlacementOnOtherTileWithPortal() {
        Game game = TestUtils.initGameEnv(5, false, true);

        while (!((GameTurnPlaceTileState) game.getState()).getTileDrawn().hasPortal()) {
            TestUtils.placeTileRandomly(game);
            TestUtils.skipStateIfNeeded(game, GameStateType.TURN_PLACE_MEEPLE);
            TestUtils.skipStateIfNeeded(game, GameStateType.TURN_MOVE_DRAGON);
        }

        TestUtils.placeTileRandomly(game);

        assertTrue(game.executeCommand(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
    }

    @Test
    public void testMultiplePlacementsOnSameTurn() {
        Game game = TestUtils.initGameEnv(5, false, true);

        assertTrue(game.executeCommand(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.executeCommand(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
        assertFalse(game.executeCommand(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
    }
}