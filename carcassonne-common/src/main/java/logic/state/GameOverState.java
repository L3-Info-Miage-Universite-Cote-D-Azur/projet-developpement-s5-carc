package logic.state;

import logic.Game;
import logic.player.Player;
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
        System.out.println("Num areas: " + game.getBoard().getTiles().stream().flatMap(t -> t.getChunks().stream()).map(c -> c.getArea()).count());
        game.getListener().onGameOver();
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

    @Override
    public GameStateType getType() {
        return GameStateType.OVER;
    }
}
