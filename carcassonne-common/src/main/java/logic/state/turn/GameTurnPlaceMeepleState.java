package logic.state.turn;

import logic.Game;
import logic.math.Vector2;
import logic.state.GameState;
import logic.state.GameStateType;
import stream.ByteInputStream;
import stream.ByteOutputStream;
import stream.ByteStreamHelper;

/**
 * Represents the state of a game turn when a player needs to place a meeple.
 *
 * The player can complete the state by executing the {@link logic.command.PlaceMeepleCommand}
 * or by skipping the placement with the {@link logic.command.SkipMeeplePlacementCommand}.
 *
 * State skipped if the player has no meeples left.
 */
public class GameTurnPlaceMeepleState extends GameState {
    /**
     * The position of the tile placed during this turn.
     */
    private Vector2 tilePosition;

    public GameTurnPlaceMeepleState(Game game) {
        super(game);
    }

    public GameTurnPlaceMeepleState(Game game, Vector2 tilePosition) {
        super(game);
        this.tilePosition = tilePosition;
    }

    /**
     * Initializes the state.
     * The state is skipped if the current player has no meeples left.
     */
    @Override
    public void init() {
        if (game.getTurnExecutor().hasRemainingMeeples()) {
            game.getTurnExecutor().getListener().onWaitingMeeplePlacement();
        } else {
            complete();
        }
    }

    /**
     * Encodes the state to a byte stream.
     * @param stream The stream to encode to.
     */
    @Override
    public void encode(ByteOutputStream stream) {
        ByteStreamHelper.encodeVector(stream, tilePosition);
    }

    /**
     * Decodes the state from a byte stream.
     * @param stream The stream to decode from.
     */
    @Override
    public void decode(ByteInputStream stream) {
        tilePosition = ByteStreamHelper.decodeVector(stream);
    }

    /**
     * Completes the state by starting the {@link GameTurnMoveDragonState}.
     */
    @Override
    public void complete() {
        game.setState(new GameTurnMoveDragonState(game));
    }

    /**
     * Returns the type of the state.
     *
     * @return The type of the state.
     */
    @Override
    public GameStateType getType() {
        return GameStateType.TURN_PLACE_MEEPLE;
    }

    /**
     * Returns the position of the tile placed during this turn.
     * @return The position of the tile placed during this turn.
     */
    public Vector2 getTileDrawnPosition() {
        return tilePosition;
    }
}
