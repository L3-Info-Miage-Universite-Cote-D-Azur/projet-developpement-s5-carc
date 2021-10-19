package logic.tile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TileDataTest {
    @Test
    void testTileData(){
        TileType tileType = TileType.ROAD;
        TileData tileData0 = new TileData(tileType);
        assertEquals(tileType, tileData0.getType());
    }
}
