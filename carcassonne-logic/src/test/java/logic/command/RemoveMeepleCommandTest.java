package logic.command;

import logic.Game;
import logic.board.GameBoard;
import logic.config.GameConfig;
import logic.player.Player;
import logic.tile.ChunkId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RemoveMeepleCommandTest {

    @Test
    public void testRemoveOnOccupiedTile() {
        Game game = GameEnv();

        assertTrue(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.getCommandExecutor().execute(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
        assertTrue(game.getCommandExecutor().execute(new RemoveMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
    }


    private static Game GameEnv() {
        Game game = new Game(GameConfig.loadFromResources());

        game.addPlayer(new Player(1));
        game.addPlayer(new Player(2));

        game.start();

        return game;
    }
}
