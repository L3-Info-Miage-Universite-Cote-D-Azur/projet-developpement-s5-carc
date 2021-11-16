package logic.player;

import logic.tile.ChunkType;
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

        assertEquals(0, player.getScore());
        player.addScore(100, ChunkType.ABBEY);
        assertEquals(100, player.getScore());
        assertEquals(100, player.getAbbeyPoints());

        player.addScore(100, ChunkType.ROAD);
        assertEquals(200, player.getScore());
        assertEquals(100, player.getRoadPoints());

        player.addScore(100, ChunkType.FIELD);
        assertEquals(300, player.getScore());
        assertEquals(100, player.getFieldPoints());

        player.addScore(100, ChunkType.TOWN);
        assertEquals(400, player.getScore());
        assertEquals(100, player.getTownPoints());

        assertEquals(0, player.getRemainingMeepleCount());
        assertEquals(0, player.getPartisansPlayed());
        assertEquals(0, player.getPartisansRemained());

        assertThrows(IllegalArgumentException.class, () -> {
           player.addScore(-1, ChunkType.ABBEY);
        });

        assertThrows(IllegalStateException.class, () -> {
            player.addScore(10, ChunkType.RIVER);
        });
    }
}
