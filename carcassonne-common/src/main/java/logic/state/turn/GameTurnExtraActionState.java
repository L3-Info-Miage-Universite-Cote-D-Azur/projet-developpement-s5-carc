package logic.state.turn;

import logic.Game;
import logic.state.GameState;
import logic.state.GameStateType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Represents the extra turn state.
 * During this state, the player can place or remove a meeple.
 */
public class GameTurnExtraActionState extends GameState {
    private boolean hasPlacedMeeple;
    private boolean hasRemovedMeeple;

    public GameTurnExtraActionState(Game game) {
        super(game);
    }

    @Override
    public void init() {
        game.getTurnExecutor().getListener().onWaitingExtraAction();
    }

    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeBoolean(hasPlacedMeeple);
        stream.writeBoolean(hasRemovedMeeple);
    }

    @Override
    public void decode(ByteInputStream stream) {
        hasPlacedMeeple = stream.readBoolean();
        hasRemovedMeeple = stream.readBoolean();
    }

    @Override
    public void complete() {
        game.setState(game.isMaster()
                ? new GameTurnEndingState(game)
                : new GameTurnWaitingMasterDataState(game));
    }

    @Override
    public GameStateType getType() {
        return GameStateType.TURN_EXTRA_ACTION;
    }

    public boolean hasPlacedMeeple() {
        return hasPlacedMeeple;
    }

    public void setPlacedMeeple() {
        hasPlacedMeeple = true;
    }

    public boolean hasRemovedMeeple() {
        return hasRemovedMeeple;
    }

    public void setRemovedMeeple() {
        hasRemovedMeeple = true;
    }
}
