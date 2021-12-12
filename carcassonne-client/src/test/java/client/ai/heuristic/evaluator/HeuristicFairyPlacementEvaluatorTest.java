package client.ai.heuristic.evaluator;

import logic.Game;
import logic.config.GameConfig;
import logic.math.Vector2;
import logic.meeple.Meeple;
import logic.player.Player;
import logic.tile.Tile;
import logic.tile.TileRotation;
import logic.tile.chunk.ChunkId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeuristicFairyPlacementEvaluatorTest {
    private HeuristicFairyPlacementEvaluator heuristicFairyPlacementEvaluator;
    private Tile tile3;
    private Tile tile8;
    private Tile tile9;
    private Game game;

    @BeforeEach
    void setUp() {
        GameConfig config = GameConfig.loadFromResources();
        assertNotNull(config);
        game = new Game(config);
        Player player0 = new Player();
        Player player1 = new Player();
        Player player2 = new Player();
        game.addPlayer(player0);
        game.addPlayer(player1);
        game.addPlayer(player2);
        heuristicFairyPlacementEvaluator = new HeuristicFairyPlacementEvaluator(game, player0);

        Tile tile1 = config.getTiles().stream().filter(t -> t.getModel().equals("V")).findFirst().get().createTile(game);
        Tile tile2 = config.getTiles().stream().filter(t -> t.getModel().equals("V")).findFirst().get().createTile(game);
        tile3 = config.getTiles().stream().filter(t -> t.getModel().equals("V")).findFirst().get().createTile(game);
        Tile tile4 = config.getTiles().stream().filter(t -> t.getModel().equals("V")).findFirst().get().createTile(game);
        Tile tile5 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        Tile tile6 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        Tile tile7 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        tile8 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        tile9 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        Tile tile10 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        Tile tile11 = config.getTiles().stream().filter(t -> t.getModel().equals("AE")).findFirst().get().createTile(game);
        Tile tile12 = config.getTiles().stream().filter(t -> t.getModel().equals("E")).findFirst().get().createTile(game);

        tile1.setPosition(new Vector2(0, 0));
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
        tile12.setPosition(new Vector2(1, 1));

        tile1.setRotation(TileRotation.DOWN);
        tile2.setRotation(TileRotation.RIGHT);
        tile3.setRotation(TileRotation.LEFT);
        tile9.setRotation(TileRotation.LEFT);
        tile10.setRotation(TileRotation.LEFT);
        tile11.setRotation(TileRotation.DOWN);
        tile12.setRotation(TileRotation.UP);

        tile8.getChunk(ChunkId.CENTER_MIDDLE).setMeeple(new Meeple(player2));
        tile3.getChunk(ChunkId.CENTER_MIDDLE).setMeeple(new Meeple(player1));
        tile9.getChunk(ChunkId.CENTER_MIDDLE).setMeeple(new Meeple(player0));

        game.getBoard().place(tile1);
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
        game.getBoard().place(tile12);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void evaluateWithFairyNotSpawned() {
        int tile3Evaluated = heuristicFairyPlacementEvaluator.evaluate(tile3.getChunk(ChunkId.CENTER_MIDDLE));
        int tile9Evaluated = heuristicFairyPlacementEvaluator.evaluate(tile9.getChunk(ChunkId.CENTER_MIDDLE));
        int tile8Evaluated = heuristicFairyPlacementEvaluator.evaluate(tile8.getChunk(ChunkId.CENTER_MIDDLE));
        assertEquals(tile3Evaluated, tile8Evaluated);
        assertTrue(tile3Evaluated > tile9Evaluated);
        assertTrue(tile8Evaluated > tile9Evaluated);
    }

    @Test
    void evaluateWithFairySpawned() {
        //game.getBoard().spawnFairy(tile);
    }
}