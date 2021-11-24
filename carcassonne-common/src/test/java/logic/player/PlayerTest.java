package logic.player;

import logic.Game;
import logic.config.GameConfig;
import logic.tile.chunk.ChunkType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerTest {
    private static final GameConfig config = GameConfig.loadFromResources();
    @Test
    void testAddScore() {
        Player player = new Player(1);

        assertEquals(0, player.getScore());
        player.addScore(100, ChunkType.ABBEY);
        assertEquals(100, player.getScore());
        assertEquals(100, player.getAbbeyScore());

        player.addScore(100, ChunkType.ROAD);
        assertEquals(200, player.getScore());
        assertEquals(100, player.getRoadScore());

        player.addScore(100, ChunkType.FIELD);
        assertEquals(300, player.getScore());
        assertEquals(100, player.getFieldPoints());

        player.addScore(100, ChunkType.TOWN);
        assertEquals(400, player.getScore());
        assertEquals(100, player.getTownScore());

        assertEquals(0, player.getMeeplesPlayed());
        assertEquals(0, player.getMeeplesPlayed());
        assertEquals(0, player.getMeeplesRemained());

        assertThrows(IllegalArgumentException.class, () -> {
           player.addScore(-1, ChunkType.ABBEY);
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
