package logic.player;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void testAddScore() {
        PlayerBase player = new PlayerBase(1) {
            @Override
            public void onTurn() {
            }
        };

        assertEquals(0, player.score);
        player.addScore(100);
        assertEquals(100, player.score);
        player.addScore(100);
        assertEquals(200, player.score);
        assertThrows(IllegalArgumentException.class, () -> {
           player.addScore(-1);
        });
    }
}
