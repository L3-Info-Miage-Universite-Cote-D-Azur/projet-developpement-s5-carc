package logic.tile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTypeTest {
    @Test
    void TileTypeTest() {
        assertEquals(4, TileType.values().length);
        assertNotNull(TileType.valueOf("START"));
        assertNotNull(TileType.valueOf("ROAD"));
        assertNotNull(TileType.valueOf("FIELD"));
        assertNotNull(TileType.valueOf("TOWN_CHUNK"));
    }
}
