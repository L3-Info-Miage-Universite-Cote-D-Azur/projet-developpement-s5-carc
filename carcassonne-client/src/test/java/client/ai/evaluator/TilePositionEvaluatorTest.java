package client.ai.evaluator;

import logic.Game;
import logic.config.GameConfig;
import logic.math.Vector2;
import logic.player.Player;
import logic.tile.Tile;
import logic.tile.TileRotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TilePositionEvaluatorTest {
    private GameConfig config;
    private Game game;
    private TilePositionEvaluator tilePositionEvaluator;

    @BeforeEach
    void setup() {
        config = GameConfig.loadFromResources();
        game = new Game(config);
        Player player0 = new Player();
        Player player1 = new Player();
        Player player2 = new Player();
        game.addPlayer(player0);
        game.addPlayer(player1);
        game.addPlayer(player2);
        tilePositionEvaluator = new TilePositionEvaluator(game);
    }

    @Test
    void evaluateRoad() {
        Tile tile1 = config.getTiles().stream().filter(t -> t.getModel().equals("D")).findFirst().get().createTile(game);
        Tile tile2 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        Tile tile3 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);

        tile1.setPosition(new Vector2(0, 0));
        tile2.setPosition(new Vector2(-1, 0));
        tile3.setPosition(new Vector2(0, 2));

        tile1.setRotation(TileRotation.DOWN);
        tile2.setRotation(TileRotation.RIGHT);

        game.getBoard().place(tile1);

        assertTrue(tilePositionEvaluator.evaluate(tile2) > tilePositionEvaluator.evaluate(tile3));
    }

    @Test
    void evaluateTown() {
        Tile tile1 = config.getTiles().stream().filter(t -> t.getModel().equals("E")).findFirst().get().createTile(game);
        Tile tile2 = config.getTiles().stream().filter(t -> t.getModel().equals("E")).findFirst().get().createTile(game);

        tile1.setPosition(new Vector2(0, 0));
        tile2.setPosition(new Vector2(0, 1));

        game.getBoard().place(tile1);

        TileRotation bestRotation = TileRotation.values()[0];
        int maxScore = Integer.MIN_VALUE;
        for (TileRotation tileRotation : TileRotation.values()) {
            tile2.setRotation(tileRotation);
            int score = tilePositionEvaluator.evaluate(tile2);
            if (maxScore < score) {
                maxScore = score;
                bestRotation = tileRotation;
            }
        }

        assertEquals(TileRotation.DOWN, bestRotation);
    }
}