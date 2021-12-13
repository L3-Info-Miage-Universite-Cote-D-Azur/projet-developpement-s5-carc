package logic.state;

import logic.Game;
import logic.board.GameBoard;
import logic.tile.area.Area;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Represents the game over state.
 */
public class GameOverState extends GameState {
    public GameOverState(Game game) {
        super(game);
    }

    @Override
    public void init() {
        GameBoard board = game.getBoard();

        for (Area area : board.getAreas()) {
            if (area.isWaitingOpeningEvaluation()) {
                area.evaluateOpening();
            }
        }

        game.getListener().onGameEnded();
    }

    /**
     * Encodes the state to a byte stream.
     *
     * @param stream The stream to encode to.
     */
    @Override
    public void encode(ByteOutputStream stream) {
        // Nothing to encode.
    }

    /**
     * Decodes the state from a byte stream.
     *
     * @param stream The stream to decode from.
     */
    @Override
    public void decode(ByteInputStream stream) {
        // Nothing to decode.
    }

    @Override
    public void complete() {
        throw new IllegalStateException("Cannot complete this state.");
    }

    @Override
    public GameStateType getType() {
        return GameStateType.OVER;
    }
}
