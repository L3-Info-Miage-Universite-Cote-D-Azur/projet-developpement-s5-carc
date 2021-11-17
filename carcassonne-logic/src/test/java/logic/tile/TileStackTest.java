package logic.tile;

import logic.config.GameConfig;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TileStackTest {
    private static final GameConfig config = GameConfig.loadFromResources();

    @Test
    void testRemove() {
        TileStack tileStack = new TileStack();
        Tile testTile = config.tiles.get(0).createTile();

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
        GameConfig newConfig = new GameConfig(config.tiles.stream().map(e -> {
            e.count = 100;
            return e;
        }).collect(Collectors.toCollection(ArrayList::new)), config.minPlayers, config.maxPlayers, config.startingMeepleCount);


        TileStack stack = new TileStack();
        stack.fill(newConfig);

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
        stack.fill(config);
        stack.shuffle();

        assertTrue(stack.remove().hasFlags(TileFlags.STARTING));
    }
}
