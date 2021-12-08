package logic;

import logic.board.GameBoard;
import logic.command.MoveDragonCommand;
import logic.command.PlaceMeepleCommand;
import logic.command.PlaceTileDrawnCommand;
import logic.command.SkipMeeplePlacementCommand;
import logic.config.GameConfig;
import logic.math.Vector2;
import logic.player.IPlayerListener;
import logic.player.Player;
import logic.state.GameState;
import logic.state.GameStateType;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.Direction;
import logic.tile.Tile;
import logic.tile.TileFlags;
import logic.tile.TileRotation;
import logic.tile.chunk.ChunkId;

import java.util.LinkedList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtils {
    public static Game initGameEnv(int numPlayers, boolean attachFakeAI, boolean startGame) {
        Game game = new Game(GameConfig.loadFromResources());
        FakeAI fakeAI = new FakeAI(game);

        for (int i = 0; i < numPlayers; i++) {
            game.addPlayer(new Player(i + 1) {{
                if (attachFakeAI) {
                    setListener(fakeAI);
                }
            }});
        }

        if (startGame) {
            game.start();

            if (attachFakeAI) {
                if (game.getState().getType() != GameStateType.OVER) {
                    throw new IllegalStateException("Game state is not OVER");
                }
            }
        }

        return game;
    }

    public static void assertState(Game game, GameStateType stateType) {
        assertEquals(stateType, game.getState().getType());
    }

    public static void placeTileRandomly(Game game) {
        GameState state = game.getState();

        if (state.getType() != GameStateType.TURN_PLACE_TILE) {
            throw new IllegalStateException("Game state is not TURN_PLACE_TILE");
        }

        IPlayerListener originalListener = game.getTurnExecutor().getListener();

        try {
            game.getTurnExecutor().setListener(new FakeAI(game) {
                @Override
                public void onWaitingMeeplePlacement() {

                }

                @Override
                public void onWaitingDragonMove() {

                }
            });
            game.getTurnExecutor().getListener().onWaitingPlaceTile();
        } finally {
            game.getTurnExecutor().setListener(originalListener);
        }
    }

    public static void skipStateIfNeeded(Game game, GameStateType type) {
        if (game.getState().getType() == type) {
            game.getState().complete();
        }
    }

    private static class FakeAI implements IPlayerListener {
        private final Game game;
        private final Random random = new Random();

        private Vector2 lastTilePos;

        public FakeAI(Game game) {
            this.game = game;
        }

        @Override
        public void onWaitingPlaceTile() {
            GameTurnPlaceTileState placeTileState = ((GameTurnPlaceTileState) game.getState());
            Tile tile = placeTileState.getTileDrawn();
            Vector2 pos = findPositionForTile(tile);

            lastTilePos = pos;
            assertTrue(game.executeCommand(new PlaceTileDrawnCommand(pos)));
        }

        @Override
        public void onWaitingMeeplePlacement() {
            if (random.nextInt(100) < 95 || !game.executeCommand(new PlaceMeepleCommand(lastTilePos, ChunkId.values()[random.nextInt(ChunkId.values().length)]))) {
                game.executeCommand(new SkipMeeplePlacementCommand());
            }
        }

        @Override
        public void onWaitingDragonMove() {
            do {
                if (game.executeCommand(new MoveDragonCommand(Direction.values()[random.nextInt(Direction.values().length)]))) {
                    break;
                }
            } while (true);
        }

        private Vector2 findPositionForTile(Tile tile) {
            if (tile.hasFlag(TileFlags.STARTING)) {
                return GameBoard.STARTING_TILE_POSITION;
            }

            for (int i = 0; i < TileRotation.NUM_ROTATIONS; i++) {
                tile.rotate();
                LinkedList<Vector2> positions = game.getBoard().findFreePlacesForTile(tile);

                if (!positions.isEmpty()) {
                    return positions.get(random.nextInt(positions.size()));
                }
            }

            return null;
        }
    }
}
