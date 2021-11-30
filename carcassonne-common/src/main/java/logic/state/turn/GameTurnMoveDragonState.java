package logic.state.turn;

import logic.Game;
import logic.board.GameBoard;
import logic.state.GameState;
import logic.state.GameStateType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Represents the state of a game turn when a player needs to move the dragon.
 * The state is completed if there is no dragon on the board.
 * The player can complete the state by executing the {@link logic.command.MoveDragonCommand}.
 */
public class GameTurnMoveDragonState extends GameState {
    public GameTurnMoveDragonState(Game game) {
        super(game);
    }

    @Override
    public void init() {
        GameBoard board = game.getBoard();

        if (board.hasDragon()) {
            if (board.getDragon().isBlocked()) {
                board.destructDragon();
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
     * @param stream The stream to encode to.
     */
    @Override
    public void encode(ByteOutputStream stream) {
    }

    /**
     * Decodes the state from a byte stream.
     * @param stream The stream to decode from.
     */
    @Override
    public void decode(ByteInputStream stream) {
    }

    /**
     * Completes the state.
     */
    @Override
    public void complete() {
        game.setState(game.isMaster()
                ? new GameTurnEndingState(game)
                : new GameTurnWaitingMasterDataState(game));
    }

    @Override
    public GameStateType getType() {
        return GameStateType.TURN_MOVE_DRAGON;
    }
}
