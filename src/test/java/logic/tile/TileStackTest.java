package logic.tile;

import logic.Game;
import logic.config.GameConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileStackTest {
    private static final String config0 = "{\"MIN_PLAYERS\":2,\"MAX_PLAYERS\":5,\"PLAYER_DECK_CAPACITY\":25,\"TILES\":{\"START\": { \"DECK_COUNT\": 5 }, \"ROAD\": { \"DECK_COUNT\": 5 }, \"RIVER\": { \"DECK_COUNT\": 5 }, \"TOWN_CHUNK\": { \"DECK_COUNT\": 5 }}}";
    private static final String config1 = "{\"MIN_PLAYERS\":-5,\"MAX_PLAYERS\":5,\"PLAYER_DECK_CAPACITY\":25,\"TILES\":{\"START\": { \"DECK_COUNT\": 5 }, \"ROAD\": { \"DECK_COUNT\": 5 }, \"RIVER\": { \"DECK_COUNT\": 5 }, \"TOWN_CHUNK\": { \"DECK_COUNT\": 5 }}}";
    @Test @Disabled
    void testPick(){
        TileStack tileStack = new TileStack();
        assertEquals(0, tileStack.getNumTiles());
        TileData tileData = new TileData(TileType.ROAD);

        // TODO Need to fill from config
        assertEquals(1, tileStack.getNumTiles());
        assertEquals(tileData, tileStack.pick());
    }

    @Test @Disabled
    void testFill(){

    }

    @Test @Disabled
    void testShuffle(){ // If the shuffle works properly

    }

    @Test @Disabled
    void testIsFirstTileIsStartTile(){ // If the first tile is the starting tile
        TileType tileType = TileType.START;
        assertEquals(tileType, roadTile.getType());
    }

    @Test @Disabled
    void testInitConfig(){ // If the initialization for the config works properly
        GameConfig config =
        Game game = new Game(config);
        assertEquals(config, config.validate());
    }
}
