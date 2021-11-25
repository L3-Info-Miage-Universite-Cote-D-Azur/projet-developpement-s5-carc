package logic;

import logic.board.GameBoard;
import logic.command.EndTurnCommand;
import logic.command.PlaceMeepleCommand;
import logic.command.PlaceTileDrawnCommand;
import logic.config.GameConfig;
import logic.math.Vector2;
import logic.player.IPlayerListener;
import logic.player.Player;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.Tile;
import logic.tile.TileFlags;
import logic.tile.TileRotation;
import logic.tile.chunk.ChunkId;

import java.util.LinkedList;
import java.util.Random;

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
        }

        return game;
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

            game.getCommandExecutor().execute(new PlaceTileDrawnCommand(pos));
            lastTilePos = pos;
        }

        @Override
        public void onWaitingExtraAction() {
            game.getCommandExecutor().execute(new PlaceMeepleCommand(lastTilePos, ChunkId.values()[random.nextInt(ChunkId.values().length)]));
            game.getCommandExecutor().execute(new EndTurnCommand());
        }

        private Vector2 findPositionForTile(Tile tile) {
            if (tile.hasFlags(TileFlags.STARTING)) {
                return GameBoard.STARTING_TILE_POSITION;
            }

            TileRotation originalRotation = tile.getRotation();

            for (int i = 0; i < TileRotation.NUM_ROTATIONS; i++) {
                tile.rotate();
                LinkedList<Vector2> positions = game.getBoard().findFreePlacesForTile(tile);

                if (!positions.isEmpty()) {
                    tile.setRotation(originalRotation);
                    return positions.get(random.nextInt(positions.size()));
                }
            }

            return null;
        }
    }
}
