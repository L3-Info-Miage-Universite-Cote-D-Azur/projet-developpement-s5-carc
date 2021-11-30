package logic.state.turn;

import logic.Game;
import logic.math.Vector2;
import logic.state.GameState;
import logic.state.GameStateType;
import logic.tile.Tile;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Represents the extra turn state.
 * During this state, the player can place or remove a meeple.
 */
public class GameTurnExtraActionState extends GameState {
    private Tile tileDrawn;
    private boolean hasPlacedMeeple;
    private boolean hasRemovedMeeple;

    public GameTurnExtraActionState(Game game) {
        super(game);
    }

    public GameTurnExtraActionState(Game game, Tile tileDrawn) {
        super(game);
        this.tileDrawn = tileDrawn;
    }

    @Override
    public void init() {
        game.getTurnExecutor().getListener().onWaitingExtraAction();
    }

    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeInt(tileDrawn.getPosition().getX());
        stream.writeInt(tileDrawn.getPosition().getY());
        stream.writeBoolean(hasPlacedMeeple);
        stream.writeBoolean(hasRemovedMeeple);
    }

    @Override
    public void decode(ByteInputStream stream) {
        tileDrawn = game.getBoard().getTileAt(new Vector2(stream.readInt(), stream.readInt()));
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

    public Tile getTileDrawn() {
        return tileDrawn;
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
