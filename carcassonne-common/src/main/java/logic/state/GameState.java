package logic.state;

import logic.Game;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Represents the state of the game.
 */
public abstract class GameState {
    protected final Game game;

    public GameState(Game game) {
        this.game = game;
    }

    /**
     * Inits the state.
     */
    public abstract void init();

    /**
     * Encodes the state to a byte stream.
     *
     * @param stream The stream to encode to.
     */
    public abstract void encode(ByteOutputStream stream);

    /**
     * Decodes the state from a byte stream.
     *
     * @param stream The stream to decode from.
     */
    public abstract void decode(ByteInputStream stream);

    /**
     * Completes the state.
     */
    public abstract void complete();

    /**
     * Returns the type of the state.
     *
     * @return The type of the state.
     */
    public abstract GameStateType getType();
}
