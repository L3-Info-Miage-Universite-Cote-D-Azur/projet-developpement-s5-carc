package logic.tile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTypeTest {
    @Test
    void TileTypeTest() {
        String[] types = {"START", "ROAD", "TOWN_CHUNK", "RIVER","ABBEY","TOWN"};
        assertEquals(types.length, TileType.values().length);
        for (String type : types)
            assertNotNull(TileType.valueOf(type));
    }
}
