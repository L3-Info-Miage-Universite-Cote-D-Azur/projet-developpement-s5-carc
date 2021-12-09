package logic.tile;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import logic.config.GameConfig;
import logic.math.Vector2;
import logic.tile.area.Area;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {
    @Test
    void testRotate() {
        GameConfig config = GameConfig.loadFromResources();
        assertNotNull(config);
        Tile tile = config.getTile(0).createTile(null);

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
    void testAreasInTile() {
        Game game = TestUtils.initGameEnv(2, true, true);
        Tile tile = game.getBoard().getTileAt(GameBoard.STARTING_TILE_POSITION);

        ArrayList<Area> areasFound = new ArrayList<>();

        for (ChunkId chunkId : ChunkId.values()) {
            Chunk chunk = tile.getChunk(chunkId);

            if (!areasFound.contains(chunk.getArea())) {
                areasFound.add(chunk.getArea());
            }
        }

        List<Area> areasReturned = tile.getAreas();

        assertEquals(areasFound.size(), areasReturned.size());

        for (Area area : areasFound) {
            assertTrue(areasReturned.contains(area));
        }
    }
}
