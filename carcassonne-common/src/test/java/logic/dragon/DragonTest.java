package logic.dragon;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import logic.math.Vector2;
import logic.meeple.Meeple;
import logic.tile.Direction;
import logic.tile.Tile;
import logic.tile.chunk.ChunkId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DragonTest {
    @Test
    void testMoveTo() {
        Game game = TestUtils.initGameEnv(2, true, true);
        GameBoard board = game.getBoard();

        Dragon dragon = board.spawnDragon(GameBoard.STARTING_TILE_POSITION);
        assertEquals(GameBoard.STARTING_TILE_POSITION, dragon.getPosition());

        Vector2 newPosition = null;

        for (Direction direction : Direction.values()) {
            newPosition = dragon.getPosition().add(direction.value());

            if (dragon.canMoveTo(newPosition)) {
                Tile tile = board.getTileAt(newPosition);

                tile.getChunk(ChunkId.CENTER_MIDDLE).setMeeple(new Meeple(game.getPlayer(0)));
                tile.getChunk(ChunkId.EAST_BOTTOM).setMeeple(new Meeple(game.getPlayer(1)));

                break;
            }
        }

        dragon.moveTo(newPosition);

        assertEquals(newPosition, dragon.getPosition());

        Tile newTile = board.getTileAt(newPosition);

        assertEquals(null, newTile.getChunk(ChunkId.CENTER_MIDDLE).getMeeple());
        assertEquals(null, newTile.getChunk(ChunkId.EAST_BOTTOM).getMeeple());
    }
}
