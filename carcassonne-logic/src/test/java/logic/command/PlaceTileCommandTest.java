package logic.command;

import logic.Game;
import logic.board.GameBoard;
import logic.player.SimpleAIPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceTileCommandTest {
    private static final GameConfig config = GameConfig.loadFromJSON("{\"MIN_PLAYERS\":2,\"MAX_PLAYERS\":5,\"TILES\":[{\"center\":{\"type\":\"ROAD\",\"relations\":[\"LEFT\",\"RIGHT\"]},\"top\":{\"type\":\"TOWN_WALL\",\"relations\":[]},\"bot\":{\"type\":\"FIELD\",\"relations\":[]},\"left\":{\"type\":\"ROAD\",\"relations\":[\"CENTER\"]},\"right\":{\"type\":\"ROAD\",\"relations\":[\"CENTER\"]},\"details\":{\"model\":\"D\",\"count\":1,\"expansion\":\"default\",\"flags\":[\"STARTING\"]}},{\"center\":{\"type\":\"ROAD\",\"relations\":[\"LEFT\",\"RIGHT\"]},\"top\":{\"type\":\"TOWN_WALL\",\"relations\":[]},\"bot\":{\"type\":\"FIELD\",\"relations\":[]},\"left\":{\"type\":\"ROAD\",\"relations\":[\"CENTER\"]},\"right\":{\"type\":\"ROAD\",\"relations\":[\"CENTER\"]},\"details\":{\"model\":\"D\",\"count\":3,\"expansion\":\"default\",\"flags\":[]}}]}");

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
