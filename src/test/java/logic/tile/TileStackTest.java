package logic.tile;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileStackTest {
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
}