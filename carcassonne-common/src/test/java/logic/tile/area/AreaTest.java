package logic.tile.area;

import logic.Game;
import logic.config.GameConfig;
import logic.math.Vector2;
import logic.tile.Tile;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AreaTest {
    @Test
    void testClosure() {
        GameConfig config = GameConfig.loadFromResources();
        assertNotNull(config);
        Game game = new Game(config);

        Tile tile1 = config.getTiles().stream().filter(t -> t.getModel().equals("H")).findFirst().get().createTile(game);
        Tile tile2 = config.getTiles().stream().filter(t -> t.getModel().equals("F")).findFirst().get().createTile(game);
        Tile tile3 = config.getTiles().stream().filter(t -> t.getModel().equals("I")).findFirst().get().createTile(game);

        tile1.setPosition(new Vector2(0, 0));
        tile2.setPosition(new Vector2(1, 0));
        tile3.setPosition(new Vector2(2, 0));

        game.getBoard().place(tile1);
        game.getBoard().place(tile2);
        game.getBoard().place(tile3);

        assertTrue(tile1.getChunk(ChunkId.EAST_MIDDLE).getArea().isClosed());
        assertFalse(tile1.getChunk(ChunkId.WEST_MIDDLE).getArea().isClosed());
    }

    @Test
    void testBadMerging() {
        GameConfig config = GameConfig.loadFromResources();
        assertNotNull(config);
        Game game = new Game(config);

        Tile tile = config.getTiles().stream().filter(t -> t.getModel().equals("H")).findFirst().get().createTile(game);

        Chunk fieldChunk = tile.getChunk(ChunkId.CENTER_MIDDLE);
        Chunk townChunk = tile.getChunk(ChunkId.EAST_MIDDLE);

        Area area1 = new FieldArea(List.of(fieldChunk));
        Area area2 = new TownArea(List.of(townChunk));

        assertThrows(IllegalArgumentException.class, () -> area1.merge(area2));
    }
}
