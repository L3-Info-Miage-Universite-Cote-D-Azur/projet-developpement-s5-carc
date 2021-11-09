package logic.tile;

import logic.config.GameConfig;
import logic.config.TileConfig;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/*class TileStackTest {
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
        GameConfig config = new GameConfig() {{
            TILES = new ArrayList<>() {{

            }};
        }};

        TileStack stack = new TileStack();
        stack.fill(config);

        HashMap<TileType, Integer> tileCountByType = new HashMap<>();

        while (stack.getNumTiles() >= 1) {
            TileType tileType = stack.remove().getType();

            if (tileCountByType.containsKey(tileType)) {
                tileCountByType.replace(tileType, tileCountByType.get(tileType) + 1);
            } else {
                tileCountByType.put(tileType, 1);
            }
        }

        for (Map.Entry<TileType, TileConfig> e : config.TILES.entrySet()) {
            assertEquals(e.getValue().DECK_COUNT, tileCountByType.getOrDefault(e.getKey(), 0));
        }
    }

    @Test
    void testShuffle() { // If the shuffle works properly
        GameConfig config = new GameConfig() {{
            TILES = new HashMap<>() {{
                put(TileType.ROAD, new TileConfig() {{
                    DECK_COUNT = 1000;
                }});
                put(TileType.ABBEY, new TileConfig() {{
                    DECK_COUNT = 1000;
                }});
            }};
        }};

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
            add(new RoadTile());
            add(new RoadTile());
            add(new StartingTile());
            add(new RoadTile());
            add(new RoadTile());
        }});
        stack.shuffle();

        assertEquals(stack.remove().getType(), TileType.START);
    }

    @Test
    void testInitConfig() { // If the initialization for the config works properly
        GameConfig config = new GameConfig() {{
            TILES = new HashMap<>() {{
               put(TileType.ROAD, new TileConfig() {{
                   DECK_COUNT = 1000;
               }});
               put(TileType.ABBEY, new TileConfig() {{
                   DECK_COUNT = 1000;
               }});
            }};
        }};

        TileStack stack = new TileStack();
        stack.fill(config);

        HashMap<TileType, Integer> tileCountByType = new HashMap<>();

        while (stack.getNumTiles() >= 1) {
            TileType tileType = stack.remove().getType();

            if (tileCountByType.containsKey(tileType)) {
                tileCountByType.replace(tileType, tileCountByType.get(tileType) + 1);
            } else {
                tileCountByType.put(tileType, 1);
            }
        }

        for (Map.Entry<TileType, TileConfig> e : config.TILES.entrySet()) {
            assertEquals(e.getValue().DECK_COUNT, tileCountByType.getOrDefault(e.getKey(), 0));
        }
    }
}*/
