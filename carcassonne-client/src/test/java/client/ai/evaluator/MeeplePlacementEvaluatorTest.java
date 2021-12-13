package client.ai.evaluator;

import logic.Game;
import logic.config.GameConfig;
import logic.math.Vector2;
import logic.player.Player;
import logic.tile.Tile;
import logic.tile.TileRotation;
import logic.tile.chunk.ChunkId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MeeplePlacementEvaluatorTest {
    private MeeplePlacementEvaluator meeplePlacementEvaluator;
    private Tile tile9;
    private Tile tile11;

    @BeforeEach
    void setUp() {
        GameConfig config = GameConfig.loadFromResources();
        Game game = new Game(config);
        Player player0 = new Player();
        Player player1 = new Player();
        Player player2 = new Player();
        game.addPlayer(player0);
        game.addPlayer(player1);
        game.addPlayer(player2);
        meeplePlacementEvaluator = new MeeplePlacementEvaluator(player0);

        Tile tile2 = config.getTiles().stream().filter(t -> t.getModel().equals("V")).findFirst().get().createTile(game);
        Tile tile3 = config.getTiles().stream().filter(t -> t.getModel().equals("V")).findFirst().get().createTile(game);
        Tile tile4 = config.getTiles().stream().filter(t -> t.getModel().equals("V")).findFirst().get().createTile(game);
        Tile tile5 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        Tile tile6 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        Tile tile7 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        Tile tile8 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        tile9 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        Tile tile10 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        tile11 = config.getTiles().stream().filter(t -> t.getModel().equals("E")).findFirst().get().createTile(game);

        tile2.setPosition(new Vector2(2, 0));
        tile3.setPosition(new Vector2(0, 3));
        tile4.setPosition(new Vector2(2, 3));
        tile5.setPosition(new Vector2(0, 1));
        tile6.setPosition(new Vector2(0, 2));
        tile7.setPosition(new Vector2(2, 1));
        tile8.setPosition(new Vector2(2, 2));
        tile9.setPosition(new Vector2(1, 0));
        tile10.setPosition(new Vector2(1, 3));
        tile11.setPosition(new Vector2(1, 2));

        tile2.setRotation(TileRotation.RIGHT);
        tile3.setRotation(TileRotation.LEFT);
        tile9.setRotation(TileRotation.LEFT);
        tile10.setRotation(TileRotation.LEFT);
        tile11.setRotation(TileRotation.DOWN);

        game.getBoard().place(tile2);
        game.getBoard().place(tile3);
        game.getBoard().place(tile4);
        game.getBoard().place(tile5);
        game.getBoard().place(tile6);
        game.getBoard().place(tile7);
        game.getBoard().place(tile8);
        game.getBoard().place(tile9);
        game.getBoard().place(tile10);
        game.getBoard().place(tile11);
    }

    @Test
    void evaluate() {
        int meeplePlacement9Evaluator = meeplePlacementEvaluator.evaluate(tile9.getChunk(ChunkId.CENTER_MIDDLE));
        int meeplePlacement11CMEvaluator = meeplePlacementEvaluator.evaluate(tile11.getChunk(ChunkId.CENTER_MIDDLE));
        int meeplePlacement11SMEvaluator = meeplePlacementEvaluator.evaluate(tile11.getChunk(ChunkId.SOUTH_MIDDLE));

        assertTrue(meeplePlacement11SMEvaluator < meeplePlacement9Evaluator);
        assertTrue(meeplePlacement9Evaluator < meeplePlacement11CMEvaluator);
    }
}