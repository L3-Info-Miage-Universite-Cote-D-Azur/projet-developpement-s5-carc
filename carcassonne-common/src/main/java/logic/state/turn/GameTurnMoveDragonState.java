package logic.state.turn;

import logic.Game;
import logic.board.GameBoard;
import logic.dragon.Dragon;
import logic.math.Vector2;
import logic.state.GameState;
import logic.state.GameStateType;
import logic.tile.TileFlags;
import stream.ByteInputStream;
import stream.ByteOutputStream;
import stream.ByteStreamHelper;

/**
 * Represents the state of a game turn when a player needs to move the dragon.
 * The state is completed if there is no dragon on the board.
 * The player can complete the state by executing the {@link logic.command.MoveDragonCommand}.
 */
public class GameTurnMoveDragonState extends GameState {
    private Vector2 tilePosition;

    public GameTurnMoveDragonState(Game game) {
        super(game);
    }

    public GameTurnMoveDragonState(Game game, Vector2 tilePosition) {
        super(game);
        this.tilePosition = tilePosition;
    }

    @Override
    public void init() {
        GameBoard board = game.getBoard();

        if (board.hasDragon() && board.getTileAt(tilePosition).hasFlag(TileFlags.DRAGON)) {
            Dragon dragon = board.getDragon();

            if (dragon.isBlocked() || dragon.hasFinished()) {
                board.killDragon();
                complete();
            } else {
                game.getTurnExecutor().getListener().onWaitingDragonMove();
            }
        } else {
            complete();
        }
    }

    /**
     * Encodes the state to a byte stream.
     *
     * @param stream The stream to encode to.
     */
    @Override
    public void encode(ByteOutputStream stream) {
        ByteStreamHelper.encodeVector(stream, tilePosition);
    }

    /**
     * Decodes the state from a byte stream.
     *
     * @param stream The stream to decode from.
     */
    @Override
    public void decode(ByteInputStream stream) {
        tilePosition = ByteStreamHelper.decodeVector(stream);
    }

    /**
     * Completes the state.
     */
    @Override
    public void complete() {
        game.setState(new GameTurnEndingState(game));
    }

    @Override
    public GameStateType getType() {
        return GameStateType.TURN_MOVE_DRAGON;
    }
}
