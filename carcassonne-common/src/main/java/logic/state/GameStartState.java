package logic.state;


import logic.Game;
import logic.player.Player;
import logic.state.turn.GameTurnInitState;
import logic.state.turn.GameWaitingMasterDataState;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Represents the state of the game when it's started.
 */
public class GameStartState extends GameState {
    public GameStartState(Game game) {
        super(game);
    }

    /**
     * Inits the state.
     * {@link GameStartState} initializes the game by clearing the game instance and starting the first turn.
     */
    @Override
    public void init() {
        for (Player player : game.getPlayers()) {
            player.init();
        }

        game.getBoard().clear();

        if (game.isMaster()) {
            game.getStack().fill(game.getConfig());
            game.getStack().shuffle();

            game.getListener().onGameStarted();

            game.setState(new GameTurnInitState(game));
        } else {
            game.setState(new GameWaitingMasterDataState(game));
        }
    }

    /**
     * Encodes the state to a byte stream.
     *
     * @param stream The stream to encode to.
     */
    @Override
    public void encode(ByteOutputStream stream) {
        // TODO
    }

    /**
     * Decodes the state from a byte stream.
     *
     * @param stream The stream to decode from.
     */
    @Override
    public void decode(ByteInputStream stream) {
        // TODO
    }

    /**
     * Completes the state.
     */
    @Override
    public void complete() {
        throw new IllegalStateException("Cannot complete this state.");
    }

    /**
     * Gets the type of the state.
     *
     * @return The type of the state.
     */
    @Override
    public GameStateType getType() {
        return GameStateType.START;
    }
}
