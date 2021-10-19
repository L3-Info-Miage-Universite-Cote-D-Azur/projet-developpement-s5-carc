package logic.tile;

import logic.math.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileFactoryTest {
    @Test
    void testCreate(){
        Vector2 vector0 = new Vector2(10,15);
        TileType tileType0 = TileType.ROAD;
        TileData tileData = new TileData(tileType0);
        Tile tile0 = TileFactory.create(tileData, vector0);
        assertEquals(vector0, tile0.getPosition());
        assertEquals(tileType0, tile0.getType());
    }
}
