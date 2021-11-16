package logic.command;

import logic.Game;
import logic.board.GameBoard;
import logic.config.GameConfig;
import logic.player.SimpleAIPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceTileCommandTest {
    private static final GameConfig config = GameConfig.loadFromDirectory("config");

    @Test
    void testInvalidPosition() {
        Game game = createGameEnv();

        assertTrue(new PlaceTileCommand(game.getStack().remove(), GameBoard.STARTING_TILE_POSITION).execute(game));
        assertFalse(new PlaceTileCommand(game.getStack().remove(), GameBoard.STARTING_TILE_POSITION.add(10, 10)).execute(game));
    }

    @Test
    void testNoStartingTilePlacement() {
        Game game = createGameEnv();
        assertFalse(new PlaceTileCommand(game.getStack().remove(), GameBoard.STARTING_TILE_POSITION.add(10, 10)).execute(game));
    }

    @Test
    void testOverlapTilePlacement() {
        Game game = createGameEnv();
        assertTrue(new PlaceTileCommand(game.getStack().remove(), GameBoard.STARTING_TILE_POSITION).execute(game));
        assertFalse(new PlaceTileCommand(game.getStack().remove(), GameBoard.STARTING_TILE_POSITION).execute(game));
    }

    private static Game createGameEnv() {
        Game game = new Game(config);

        game.addPlayer(new SimpleAIPlayer(0));
        game.addPlayer(new SimpleAIPlayer(1));

        game.start();

        return game;
    }
}
