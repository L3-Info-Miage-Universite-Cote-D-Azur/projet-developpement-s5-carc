package logic.command;

import logic.Game;
import logic.state.GameStateType;
import logic.state.turn.GameTurnExtraActionState;
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
        return game.getState() instanceof GameTurnExtraActionState;
    }

    /**
     * Gets the game state required to execute the command.
     * @return the game state
     */
    @Override
    public GameStateType getRequiredState() {
        return GameStateType.TURN_EXTRA_ACTION;
    }

    /**
     * Executes the command to end the turn.
     * If the game instance is a master instance, the command will end the turn.
     * If the game instance is a slave instance, the command will do nothing as we need the data of the next state.
     * @param game the game context
     */
    @Override
    public void execute(Game game) {
        game.getState().complete();
    }
}
