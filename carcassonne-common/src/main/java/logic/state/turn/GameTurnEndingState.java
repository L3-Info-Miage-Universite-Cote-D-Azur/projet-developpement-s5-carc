package logic.state.turn;

import logic.Game;
import logic.state.GameState;
import logic.state.GameStateType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Represents the state of the game when the turn is ending.
 */
public class GameTurnEndingState extends GameState {
    public GameTurnEndingState(Game game) {
        super(game);
    }

    /**
     * Initializes the state by starting the next turn.
     */
    @Override
    public void init() {
        game.getBoard().checkAreaClosures();
        game.getListener().onTurnEnded(game.getTurnCount());
        game.setState(new GameTurnInitState(game));
    }

    /**
     * Encodes the state to a byte stream.
     * @param stream The stream to encode to.
     */
    @Override
    public void encode(ByteOutputStream stream) {

    }

    /**
     * Decodes the state from a byte stream.
     * @param stream The stream to decode from.
     */
    @Override
    public void decode(ByteInputStream stream) {

    }

    /**
     * Should not be called.
     */
    @Override
    public void complete() {
        throw new IllegalStateException("Cannot complete this state.");
    }

    /**
     * Returns the type of the state.
     *
     * @return the type of the state
     */
    @Override
    public GameStateType getType() {
        return GameStateType.TURN_ENDING;
    }
}
