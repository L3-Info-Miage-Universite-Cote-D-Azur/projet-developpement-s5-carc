package logic.meeple;

import logic.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MeepleTest {
    @Test
    void testOwner() {
        Player player = new Player(1);
        Meeple meeple = new Meeple(player);
        assertEquals(player, meeple.getOwner());
    }
}
