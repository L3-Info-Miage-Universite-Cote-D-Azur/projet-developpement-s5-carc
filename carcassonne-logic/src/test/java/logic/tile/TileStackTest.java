package logic.tile;

import logic.config.ChunkConfig;
import logic.config.GameConfig;
import logic.config.TileConfig;
import logic.config.TileData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

class TileStackTest {
    private static final GameConfig config = GameConfig.loadFromJSON("{\"MIN_PLAYERS\":2,\"MAX_PLAYERS\":5,\"TILES\":[{\"center\":{\"type\":\"ROAD\",\"relations\":[\"LEFT\",\"RIGHT\"]},\"top\":{\"type\":\"TOWN_WALL\",\"relations\":[]},\"down\":{\"type\":\"FIELD\",\"relations\":[]},\"left\":{\"type\":\"ROAD\",\"relations\":[\"CENTER\"]},\"right\":{\"type\":\"ROAD\",\"relations\":[\"CENTER\"]},\"details\":{\"model\":\"D\",\"count\":1,\"expansion\":\"default\",\"flags\":[\"STARTING\"]}},{\"center\":{\"type\":\"ROAD\",\"relations\":[\"LEFT\",\"RIGHT\"]},\"top\":{\"type\":\"TOWN_WALL\",\"relations\":[]},\"bot\":{\"type\":\"FIELD\",\"relations\":[]},\"left\":{\"type\":\"ROAD\",\"relations\":[\"CENTER\"]},\"right\":{\"type\":\"ROAD\",\"relations\":[\"CENTER\"]},\"details\":{\"model\":\"D\",\"count\":3,\"expansion\":\"default\",\"flags\":[]}}]}");

    @Test
    void testRemove() {
        TileStack tileStack = new TileStack();
        Tile testTile = new Tile(new TileData("A", 1, "default", EnumSet.noneOf(TileFlags.class)));

        tileStack.fill(new ArrayList<>() {{
            add(testTile);
        }});

        assertEquals(1, tileStack.getNumTiles());
        assertEquals(testTile, tileStack.remove());
    }

    @Test
    void testFill() {
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
                /*center = new ChunkConfig(ChunkType.FIELD, new ChunkOffset[0]);
                top = new ChunkConfig(ChunkType.FIELD, new ChunkOffset[0]);
                bot = new ChunkConfig(ChunkType.FIELD, new ChunkOffset[0]);
                left = new ChunkConfig(ChunkType.FIELD, new ChunkOffset[0]);
                right = new ChunkConfig(ChunkType.FIELD, new ChunkOffset[0]);*/
                details = new TileData("A", 100, "default", EnumSet.noneOf(TileFlags.class));
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
            add(new Tile(new TileData("A", 1, "default", EnumSet.noneOf(TileFlags.class))));
            add(new Tile(new TileData("A", 1, "default", EnumSet.of(TileFlags.STARTING))));
            add(new Tile(new TileData("A", 1, "default", EnumSet.noneOf(TileFlags.class))));
            add(new Tile(new TileData("A", 1, "default", EnumSet.noneOf(TileFlags.class))));
        }});
        stack.shuffle();

        assertTrue(stack.remove().hasFlags(TileFlags.STARTING));
    }
}
