package logic.command;

import logic.Game;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Interface for all commands.
 */
public interface ICommand {
    /**
     * Gets the command type.
     * @return the command type
     */
    CommandType getType();

    /**
     * Encodes the command attributes to the output stream.
     * @param stream the output stream
     */
    void encode(ByteOutputStream stream);

    /**
     * Decodes the command attributes from the input stream.
     * @param stream the input stream
     */
    void decode(ByteInputStream stream);

    /**
     * Checks if the command is valid and can be executed.
     * @return true if the command is valid
     */
    boolean canBeExecuted(Game game);

    /**
     * Executes the command.
     * @param game the game context
     */
    void execute(Game game);
}
