package logic.tile;

import logic.config.GameConfig;
import logic.config.TileConfig;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TileStackTest {
    @Test
    void testRemove() {
        TileStack tileStack = new TileStack();
        Tile testTile = new Tile();

        tileStack.fill(new ArrayList<>() {{
            add(testTile);
        }});

        assertEquals(1, tileStack.getNumTiles());
        assertEquals(testTile, tileStack.remove());
    }

    @Test
    void testFill() {
        GameConfig config = new GameConfig();

        TileStack stack = new TileStack();
        stack.fill(config);

        // TODO
    }

    @Test
    void testShuffle() { // If the shuffle works properly
        GameConfig config = new GameConfig();

        config.TILES = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            config.TILES.add(new TileConfig() {{
                center = ChunkType.FIELD;
                up = ChunkType.FIELD;
                down = ChunkType.FIELD;
                left = ChunkType.FIELD;
                right = ChunkType.FIELD;
            }});
        }

        TileStack stack = new TileStack();

        stack.fill(config);

        ArrayList<Tile> originalTilesPicked = new ArrayList<>();

        while (stack.getNumTiles() >= 1) {
            originalTilesPicked.add(stack.remove());
        }

        stack.fill(originalTilesPicked);
        stack.shuffle();

        ArrayList<Tile> shuffledTilesPicked = new ArrayList<>();

        while (stack.getNumTiles() >= 1) {
            shuffledTilesPicked.add(stack.remove());
        }

        assertEquals(shuffledTilesPicked.size(),originalTilesPicked.size());

        int matchCount = 0;

        for (int i = 0; i < shuffledTilesPicked.size() ; i++) {
            if(shuffledTilesPicked.get(i) == originalTilesPicked.get(i)) {
                matchCount++;
            }
        }

        double matchPercentage = 100.0 * matchCount / originalTilesPicked.size();

        assertTrue(matchPercentage < 10);
    }

    @Test
    void testIsFirstTileIsStartTile() { // If the first tile is the starting tile
        TileStack stack = new TileStack();
        stack.fill(new ArrayList<>() {{
            add(new Tile(true));
            add(new Tile(false));
            add(new Tile(false));
            add(new Tile(false));
            add(new Tile(false));
            add(new Tile(false));
        }});
        stack.shuffle();

        assertTrue(stack.remove().isStartingTile());
    }
}
