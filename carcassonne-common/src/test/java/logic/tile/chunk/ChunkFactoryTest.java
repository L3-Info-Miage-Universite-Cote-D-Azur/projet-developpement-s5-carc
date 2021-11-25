package logic.tile.chunk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChunkFactoryTest {
    @Test
    void testFactory() {
        for (ChunkType type : ChunkType.values()) {
            Chunk chunk = ChunkFactory.createByType(type, null);

            assertNotNull(chunk);
            assertEquals(type, chunk.getType());
        }
    }
}
