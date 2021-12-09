package logic.state;

import logic.Game;
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
        game.getListener().onGameEnded();
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

    @Override
    public GameStateType getType() {
        return GameStateType.OVER;
    }
}
