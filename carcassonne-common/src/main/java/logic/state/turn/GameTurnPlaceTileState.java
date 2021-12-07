package logic.state.turn;

import logic.Game;
import logic.state.GameState;
import logic.state.GameStateType;
import logic.tile.Tile;
import stream.ByteInputStream;
import stream.ByteOutputStream;
import stream.ByteStreamHelper;

/**
 * Represents the state of a game turn when a player is placing a tile.
 */
public class GameTurnPlaceTileState extends GameState {
    /**
     * The tile drawn from the stack.
     */
    private Tile tileDrawn;

    public GameTurnPlaceTileState(Game game) {
        super(game);
    }

    public GameTurnPlaceTileState(Game game, Tile tileDrawn) {
        super(game);
        this.tileDrawn = tileDrawn;
    }

    /**
     * Initializes the state by starting the next turn.
     */
    @Override
    public void init() {
        game.getTurnExecutor().getListener().onWaitingPlaceTile();
    }

    /**
     * Encodes the state to a byte stream.
     *
     * @param stream The stream to encode to.
     */
    @Override
    public void encode(ByteOutputStream stream) {
        ByteStreamHelper.encodeTile(stream, tileDrawn, game);
    }

    /**
     * Decodes the state from a byte stream.
     *
     * @param stream The stream to decode from.
     */
    @Override
    public void decode(ByteInputStream stream) {
        tileDrawn = ByteStreamHelper.decodeTile(stream, game);
    }

    /**
     * Completes the state by starting the {@link GameTurnPlaceMeepleState}.
     */
    @Override
    public void complete() {
        if (tileDrawn.getPosition() == null) {
            throw new IllegalStateException("Tile drawn was not placed.");
        }

        game.setState(new GameTurnPlaceMeepleState(game, tileDrawn.getPosition()));
    }

    /**
     * Returns the type of this state.
     *
     * @return The type of this state.
     */
    @Override
    public GameStateType getType() {
        return GameStateType.TURN_PLACE_TILE;
    }

    /**
     * Returns the tile that is being drawn.
     *
     * @return The tile that is being drawn.
     */
    public Tile getTileDrawn() {
        return tileDrawn;
    }
}
