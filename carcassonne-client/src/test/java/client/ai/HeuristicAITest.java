package client.ai;

import logic.Game;
import logic.config.GameConfig;
import logic.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class HeuristicAITest {
    private Game game;

    @BeforeEach
    void setup() {
        game = new Game(GameConfig.loadFromResources());
        game.addPlayer(new Player(1));
        game.addPlayer(new Player(2));
        game.start();
    }

    @Test
    void testThrowWhenUsingNonInGamePlayer() {
        Player player = new Player(1);
        assertThrows(IllegalArgumentException.class, () -> new HeuristicAI(player));
    }
}
