package client.ai;

import logic.math.Vector2;
import logic.tile.TileRotation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TilePositionTest {
    @Test
    void testJVMIntegrity() {
        TilePosition tp = new TilePosition(new Vector2(1, 2), TileRotation.DOWN);

        assertEquals(new Vector2(1, 2), tp.position());
        assertEquals(TileRotation.DOWN, tp.rotation());
    }
}
