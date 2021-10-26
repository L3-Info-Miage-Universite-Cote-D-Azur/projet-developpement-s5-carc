package logic.tile;

import logic.math.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileFactoryTest {
    @Test
    void testCreate(){
        Vector2 vector = new Vector2(10,15);
        TileType tileType = TileType.ROAD;
        Tile tile0 = TileFactory.createByType(tileType);
        tile0.setPosition(vector);
        assertEquals(vector, tile0.getPosition());
        assertEquals(tileType, tile0.getType());
    }
}
