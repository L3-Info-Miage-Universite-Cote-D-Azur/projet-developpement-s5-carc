package logic.command;

import logic.Game;
import logic.board.GameBoard;
import logic.dragon.Dragon;
import logic.math.Vector2;
import logic.state.GameStateType;
import logic.tile.Direction;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Command to move dragon
 */
public class MoveDragonCommand implements ICommand {
    private Direction direction;

    public MoveDragonCommand() {
    }

    public MoveDragonCommand(Direction direction) {
        this.direction = direction;
    }

    /**
     * Gets the command type
     *
     * @return the command type
     */
    @Override
    public CommandType getType() {
        return CommandType.MOVE_DRAGON;
    }

    /**
     * Encode the command attributes to the output stream
     *
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeInt(direction.ordinal());
    }

    /**
     * Decode the command attributes from the input stream.
     *
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
        direction = Direction.values()[stream.readInt()];
    }

    /**
     * Checks if the command is valid and can be executed.
     *
     * @param game
     * @return true if the command is valid
     */
    @Override
    public boolean canBeExecuted(Game game) {
        GameBoard board = game.getBoard();

        if (!board.hasDragon()) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "The dragon not exist yet.");
            return false;
        }

        Vector2 position = board.getDragon().getPosition().add(direction.value());
        Dragon dragon = game.getBoard().getDragon();

        if (!dragon.canMoveTo(position)) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "The dragon can't move to this position.");
            return false;
        }

        return true;
    }

    /**
     * Gets the game state required to execute the command.
     *
     * @return the game state
     */
    @Override
    public GameStateType getRequiredState() {
        return GameStateType.TURN_MOVE_DRAGON;
    }

    /**
     * Executes the command.
     *
     * @param game The game context
     * @return True if the tile was placed, false otherwise
     */
    @Override
    public void execute(Game game) {
        Dragon dragon = game.getBoard().getDragon();
        dragon.moveTo(dragon.getPosition().add(direction.value()));
        game.getState().complete();
    }
}
