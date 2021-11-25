package logic.state.turn;

import logic.Game;
import logic.board.GameBoard;
import logic.state.GameOverState;
import logic.state.GameState;
import logic.state.GameStateType;
import logic.tile.Tile;
import logic.tile.TileStack;
import stream.ByteInputStream;
import stream.ByteOutputStream;

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

        while (!stack.isEmpty()) {
            Tile tile = stack.remove();

            if (board.hasFreePlaceForTile(tile)) {
                game.increaseTurnCount();
                game.getListener().onTurnStarted(game.getTurnCount());
                game.setState(new GameTurnPlaceTileState(game, tile));
                return;
            }
        }

        game.setState(new GameOverState(game));
    }

    @Override
    public void encode(ByteOutputStream stream) {

    }

    @Override
    public void decode(ByteInputStream stream) {

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
