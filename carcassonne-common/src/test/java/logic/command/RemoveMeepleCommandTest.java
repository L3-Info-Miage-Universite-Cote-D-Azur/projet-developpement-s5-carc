package logic.command;

import logic.Game;
import logic.config.GameConfig;
import logic.math.Vector2;
import logic.meeple.Meeple;
import logic.player.Player;
import logic.state.turn.GameTurnPlaceMeepleState;
import logic.tile.Tile;
import logic.tile.chunk.ChunkId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RemoveMeepleCommandTest {

    @Test
    void testRemoveOnOccupiedTile() {
        GameConfig config = GameConfig.loadFromResources();
        assertNotNull(config);
        Game game = new Game(config);

        game.addPlayer(new Player(1));
        game.addPlayer(new Player(5));
        game.increaseTurnCount();

        Tile tile1 = config.tiles.stream().filter(t -> t.model.equals("S")).findFirst().get().createTile(game);

        tile1.getChunk(ChunkId.CENTER_MIDDLE).setMeeple(new Meeple(game.getPlayer(0)));
        tile1.getChunk(ChunkId.SOUTH_MIDDLE).setMeeple(new Meeple(game.getPlayer(0)));

        tile1.setPosition(new Vector2(0, 0));

        game.getBoard().place(tile1);
        game.setState(new GameTurnPlaceMeepleState(game, tile1.getPosition()));

        assertFalse(game.executeCommand(new RemoveMeepleCommand(tile1.getPosition(), ChunkId.CENTER_MIDDLE)));

        Tile tile2 = config.tiles.stream().filter(t -> t.model.equals("AA")).findFirst().get().createTile(game);

        tile2.setPosition(new Vector2(1, 0));
        game.getBoard().place(tile2);

        game.setState(new GameTurnPlaceMeepleState(game, tile2.getPosition()));

        assertFalse(game.executeCommand(new RemoveMeepleCommand(tile1.getPosition(), ChunkId.NORTH_RIGHT)));
        assertFalse(game.executeCommand(new RemoveMeepleCommand(tile1.getPosition(), ChunkId.SOUTH_MIDDLE)));
        assertTrue(game.executeCommand(new RemoveMeepleCommand(tile1.getPosition(), ChunkId.CENTER_MIDDLE)));
    }
}
