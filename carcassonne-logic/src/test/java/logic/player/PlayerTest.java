package logic.player;

import logic.Game;
import logic.config.GameConfig;
import logic.tile.ChunkType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private static final GameConfig config = GameConfig.loadFromResources();
    @Test
    void testAddScore() {
        Player player = new Player(1);

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

    @Test
    void testGetId() {
        Player player = new Player(8);
        assertEquals(8, player.getId());
    }

    @Test
    void testSetGame() {
        Player player = new Player(8);
        Game game = new Game(config);
        player.setGame(game);
        assertEquals(game, player.getGame());
    }
}
