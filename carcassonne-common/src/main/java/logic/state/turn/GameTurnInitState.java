package logic.state.turn;

import logic.Game;
import logic.board.GameBoard;
import logic.state.GameOverState;
import logic.state.GameState;
import logic.state.GameStateType;
import logic.tile.Tile;
import logic.tile.TileFlags;
import logic.tile.TileStack;
import stream.ByteInputStream;
import stream.ByteOutputStream;

import java.util.ArrayList;

/**
 * Represents the initial state of the game turn.
 */
public class GameTurnInitState extends GameState {
    public GameTurnInitState(Game game) {
        super(game);
    }

    @Override
    public void init() {
        TileStack stack = game.getStack();
        GameBoard board = game.getBoard();

        ArrayList<Tile> tilesToRefill = null;

        while (!stack.isEmpty()) {
            Tile tile = stack.remove();

            if (tile.hasFlag(TileFlags.DRAGON) && !board.hasVolcano()) {
                if (tilesToRefill == null) {
                    tilesToRefill = new ArrayList<>();
                }
                tilesToRefill.add(tile);
                continue;
            }

            if (board.hasFreePlaceForTile(tile)) {
                /* Tiles to refill are added when we find
                 * a tile to place to avoid the infinite loop. */
                if (tilesToRefill != null) {
                    stack.fill(tilesToRefill);
                    stack.shuffle();
                }

                game.increaseTurnCount();
                game.getListener().onTurnStarted(game.getTurnCount(), tile);
                game.setState(new GameTurnPlaceTileState(game, tile));
                return;
            }
        }

        game.setState(new GameOverState(game));
    }

    @Override
    public void encode(ByteOutputStream stream) {
        // TODO
    }

    @Override
    public void decode(ByteInputStream stream) {
        // TODO
    }

    @Override
    public void complete() {
        throw new IllegalStateException("Cannot complete this state.");
    }

    /**
     * Returns the type of this state.
     *
     * @return The type of this state.
     */
    @Override
    public GameStateType getType() {
        return GameStateType.TURN_INIT;
    }
}
