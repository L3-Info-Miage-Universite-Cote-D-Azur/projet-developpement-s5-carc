package logic.tile;

import logic.config.GameConfig;
import logic.config.excel.TileChunkExcelConfig;
import logic.config.excel.TileExcelConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TileTest {
    @Test
    void testRotate() {
        GameConfig config = GameConfig.loadFromResources();
        Tile tile = config.tiles.get(0).createTile();

        Chunk[] originalChunkReferences = new Chunk[ChunkId.values().length];
        Chunk[] rotatedChunkReferences = new Chunk[ChunkId.values().length];

        for (int i = 0; i< originalChunkReferences.length; i++) {
            
        }
        assertEquals(originalChunkReferences,rotatedChunkReferences);
    }


}
