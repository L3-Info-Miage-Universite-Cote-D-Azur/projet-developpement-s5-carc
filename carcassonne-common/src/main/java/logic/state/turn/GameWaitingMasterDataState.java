package logic.state.turn;

import logic.Game;
import logic.state.GameState;
import logic.state.GameStateType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Represents the state of the game when the master data is being loaded.
 * The master data is required because we need to know the next tile in
 * the stack to start the next turn.
 */
public class GameWaitingMasterDataState extends GameState {
    public GameWaitingMasterDataState(Game game) {
        super(game);
    }

    @Override
    public void init() {
        // Nothing to init.
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

    /**
     * Completes the state by ending the turn.
     */
    @Override
    public void complete() {
        game.setState(new GameTurnInitState(game));
    }

    /**
     * Returns the type of the state.
     *
     * @return the type of the state
     */
    @Override
    public GameStateType getType() {
        return GameStateType.TURN_WAITING_MASTER_DATA;
    }
}
