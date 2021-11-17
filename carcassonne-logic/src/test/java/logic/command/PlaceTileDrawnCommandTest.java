package logic.command;

import logic.Game;
import logic.board.GameBoard;
import logic.config.GameConfig;
import logic.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceTileDrawnCommandTest {
    private static final GameConfig config = GameConfig.loadFromResources("config");

    @Test
    void testInvalidPosition() {
        Game game = createGameEnv();

        assertTrue(new PlaceTileDrawnCommand(game.getStack().remove(), GameBoard.STARTING_TILE_POSITION).execute(game));
        assertFalse(new PlaceTileDrawnCommand(game.getStack().remove(), GameBoard.STARTING_TILE_POSITION.add(10, 10)).execute(game));
    }

    @Test
    void testNoStartingTilePlacement() {
        Game game = createGameEnv();
        assertFalse(new PlaceTileDrawnCommand(game.getStack().remove(), GameBoard.STARTING_TILE_POSITION.add(10, 10)).execute(game));
    }

    @Test
    void testOverlapTilePlacement() {
        Game game = createGameEnv();
        assertTrue(new PlaceTileDrawnCommand(game.getStack().remove(), GameBoard.STARTING_TILE_POSITION).execute(game));
        assertFalse(new PlaceTileDrawnCommand(game.getStack().remove(), GameBoard.STARTING_TILE_POSITION).execute(game));
    }

    private static Game createGameEnv() {
        Game game = new Game(config);

        game.addPlayer(new Player(0));
        game.addPlayer(new Player(1));

        game.start();

        return game;
    }
}
