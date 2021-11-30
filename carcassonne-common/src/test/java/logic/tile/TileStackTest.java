package logic.tile;

import logic.Game;
import logic.TestUtils;
import logic.config.GameConfig;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TileStackTest {
    private static final GameConfig config = GameConfig.loadFromResources();

    @Test
    void testRemove() {
        Game game = TestUtils.initGameEnv(2, false, false);
        TileStack tileStack = game.getStack();
        Tile testTile = config.tiles.get(0).createTile(game);

        tileStack.fill(new ArrayList<>() {{
            add(testTile);
        }});

        assertEquals(1, tileStack.getNumTiles());
        assertEquals(testTile, tileStack.remove());
    }

    @Test
    void testFill() {
        Game game = TestUtils.initGameEnv(2, false, false);
        TileStack stack = game.getStack();
        stack.fill(config);
        assertEquals(config.tiles.stream().map(t -> t.count).reduce((t1, t2) -> t1 + t2).get(), stack.getNumTiles());
    }

    @Test
    void testShuffle() { // If the shuffle works properly
        GameConfig newConfig = new GameConfig(config.tiles.stream().map(e -> {
            e.count = 100;
            return e;
        }).collect(Collectors.toCollection(ArrayList::new)), config.minPlayers, config.maxPlayers, config.startingMeepleCount);

        Game game = TestUtils.initGameEnv(2, false, false);
        TileStack stack = game.getStack();
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

        assertEquals(shuffledTilesPicked.size(), originalTilesPicked.size());

        int matchCount = 0;

        for (int i = 0; i < shuffledTilesPicked.size(); i++) {
            if (shuffledTilesPicked.get(i) == originalTilesPicked.get(i)) {
                matchCount++;
            }
        }

        double matchPercentage = 100.0 * matchCount / originalTilesPicked.size();

        assertTrue(matchPercentage < 10);
    }

    @Test
    void testIsFirstTileIsStartTile() { // If the first tile is the starting tile
        Game game = TestUtils.initGameEnv(2, false, false);
        TileStack stack = new TileStack(game);
        stack.fill(config);
        stack.shuffle();

        assertTrue(stack.remove().hasFlag(TileFlags.STARTING));
    }
}
