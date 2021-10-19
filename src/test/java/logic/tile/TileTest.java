package logic.tile;
import logic.math.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {
    @Test
    void testRoadTile(){
        TileType tileType = TileType.ROAD;
        TileData tileData = new TileData(tileType);
        Vector2 vector2 = new Vector2(10,38);
        RoadTile roadTile = (RoadTile) TileFactory.create(tileData, vector2);
        assertEquals(tileType, roadTile.getType());
        assertEquals(vector2, roadTile.getPosition());
    }

    @Test
    void testStartTile(){
        TileType tileType = TileType.START;
        TileData tileData = new TileData(tileType);
        Vector2 vector2 = new Vector2(10,38);
        StartingTile startingTile = (StartingTile) TileFactory.create(tileData, vector2);
        assertEquals(tileType, startingTile.getType());
        assertEquals(vector2, startingTile.getPosition());
    }
}
