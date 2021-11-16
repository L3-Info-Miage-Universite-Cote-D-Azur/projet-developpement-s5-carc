package logic.meeple;

import logic.player.PlayerBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MeepleTest {

    @Test
    void testMeepleExist() {
        PlayerBase player = new PlayerBase(1) {
            @Override
            public void onTurn() {
            }
        };
        Meeple meeple = new Meeple(player);
        assertEquals(player, meeple.getOwner());
    }
}
