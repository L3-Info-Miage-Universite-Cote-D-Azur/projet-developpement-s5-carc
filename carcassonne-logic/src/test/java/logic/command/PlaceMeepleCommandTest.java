package logic.command;

import logic.Game;
import logic.board.GameBoard;
import logic.config.GameConfig;
import logic.math.Vector2;
import logic.player.Player;
import logic.tile.ChunkId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlaceMeepleCommandTest {
    @Test
    public void testPlacement() {
        Game game = createGameEnv();

        assertTrue(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.getCommandExecutor().execute(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
        assertTrue(game.getCommandExecutor().execute(new EndTurnCommand()));
    }

    @Test
    public void testPlacementOnOccupiedTile() {
        Game game = createGameEnv();

        assertTrue(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.getCommandExecutor().execute(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
        assertTrue(game.getCommandExecutor().execute(new EndTurnCommand()));

        assertFalse(game.isOver());

        assertFalse(game.getCommandExecutor().execute(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
    }

    @Test
    public void testMultiplePlacementsOnSameTurn() {
        Game game = createGameEnv();

        assertTrue(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.getCommandExecutor().execute(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
        assertFalse(game.getCommandExecutor().execute(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
        assertTrue(game.getCommandExecutor().execute(new EndTurnCommand()));
    }

    private static Game createGameEnv() {
        Game game = new Game(GameConfig.loadFromResources());

        game.addPlayer(new Player(0));
        game.addPlayer(new Player(1));

        game.start();

        return game;
    }
}