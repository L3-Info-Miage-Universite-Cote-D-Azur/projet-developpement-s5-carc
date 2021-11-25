package logic.tile;

import logic.config.GameConfig;
import logic.math.Vector2;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TileTest {
    @Test
    void testRotate() {
        GameConfig config = GameConfig.loadFromResources();
        Tile tile = config.tiles.get(0).createTile(null);

        Chunk[] originalChunkReferences = new Chunk[ChunkId.values().length];
        Chunk[] rotatedChunkReferences = new Chunk[ChunkId.values().length];

        for (int i = 0; i < originalChunkReferences.length; i++) {
            originalChunkReferences[i] = tile.getChunk(ChunkId.values()[i]);
        }

        tile.setRotation(TileRotation.LEFT);

        for (int i = 0; i < rotatedChunkReferences.length; i++) {
            rotatedChunkReferences[i] = tile.getChunk(ChunkId.values()[i]);
        }

        for (int i = 0; i < originalChunkReferences.length - 1; i++) {
            assertEquals(originalChunkReferences[i], rotatedChunkReferences[(i + 3 * 3) % (originalChunkReferences.length - 1)]);
        }

        assertEquals(originalChunkReferences[12], rotatedChunkReferences[12]);

        tile.setRotation(TileRotation.UP);

        for (int i = 0; i < rotatedChunkReferences.length; i++) {
            rotatedChunkReferences[i] = tile.getChunk(ChunkId.values()[i]);
        }

        for (int i = 0; i < originalChunkReferences.length - 1; i++) {
            assertEquals(originalChunkReferences[i], rotatedChunkReferences[i]);
        }

        assertEquals(originalChunkReferences[12], rotatedChunkReferences[12]);
    }

    @Test
    void testIsOnBoard() {
        Tile tile = new Tile(null, null);

        assertFalse(tile.isOnBoard());

        tile.setPosition(new Vector2(1, 2));
        assertTrue(tile.isOnBoard());
    }

    @Test
    void testArea() {
        // TODO
    }
}
