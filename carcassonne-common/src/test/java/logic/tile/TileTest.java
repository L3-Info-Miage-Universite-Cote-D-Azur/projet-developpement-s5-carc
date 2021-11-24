package logic.tile;

import logic.config.GameConfig;
import logic.config.excel.TileChunkExcelConfig;
import logic.config.excel.TileExcelConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.EnumSet;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TileTest {
    @Test
    void testRotate() {
        GameConfig config = GameConfig.loadFromResources();
        Tile tile = config.tiles.get(0).createTile(null);

        Chunk[] originalChunkReferences = new Chunk[ChunkId.values().length];
        Chunk[] rotatedChunkReferences = new Chunk[ChunkId.values().length];

        for (int i = 0; i< originalChunkReferences.length; i++) {
            originalChunkReferences[i] = tile.getChunk(ChunkId.values()[i]);
        }

        for (int i = 0; i < 3; i++) {
            tile.rotate();
        }

        for (int i = 0; i< rotatedChunkReferences.length; i++) {
            rotatedChunkReferences[i] = tile.getChunk(ChunkId.values()[i]);
        }

        for (int i = 0; i < originalChunkReferences.length - 1; i++) {
            assertEquals(originalChunkReferences[i], rotatedChunkReferences[(i + 3 * 3) % (originalChunkReferences.length - 1)]);
        }

        assertEquals(originalChunkReferences[12], rotatedChunkReferences[12]);

        tile.rotate();

        for (int i = 0; i< rotatedChunkReferences.length; i++) {
            rotatedChunkReferences[i] = tile.getChunk(ChunkId.values()[i]);
        }

        for (int i = 0; i < originalChunkReferences.length - 1; i++) {
            assertEquals(originalChunkReferences[i], rotatedChunkReferences[i]);
        }

        assertEquals(originalChunkReferences[12], rotatedChunkReferences[12]);
    }
}
