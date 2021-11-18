package logic.command;

import logic.Game;
import logic.GameTurn;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Command to end the turn.
 */
public class EndTurnCommand implements ICommand {
    /**
     * Gets the command type.
     * @return the command type
     */
    @Override
    public CommandType getType() {
        return CommandType.END_TURN;
    }

    /**
     * Encodes the command attributes to the output stream.
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
    }

    /**
     * Decodes the command attributes from the input stream.
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
    }

    /**
     * Checks if the command is valid and can be executed.
     * @return true if the command is valid
     */
    @Override
    public boolean canBeExecuted(Game game) {
        GameTurn turn = game.getTurn();

        if (turn.isOver()) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "Turn is already over!");
            return false;
        }

        if (!turn.hasPlacedTile()) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "You must place a tile before ending your turn!");
            return false;
        }

        return true;
    }

    /**
     * Executes the command to end the turn.
     * @param game the game context
     */
    @Override
    public void execute(Game game) {
        game.getTurn().endTurn();

        if (game.isMaster()) {
            game.getTurn().playTurn();
        }
    }
}
