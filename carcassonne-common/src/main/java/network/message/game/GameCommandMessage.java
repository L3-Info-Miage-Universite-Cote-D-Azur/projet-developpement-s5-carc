package network.message.game;

import logic.command.CommandFactory;
import logic.command.CommandType;
import logic.command.ICommand;
import network.message.IMessage;
import network.message.MessageType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Message sent by the server to the client to execute the specified command in their game instance.
 */
public class GameCommandMessage implements IMessage {
    private ICommand command;

    public GameCommandMessage() {
        // ignored
    }

    public GameCommandMessage(ICommand command) {
        this.command = command;
    }

    /**
     * Returns the message type.
     *
     * @return the message type
     */
    @Override
    public MessageType getType() {
        return MessageType.GAME_COMMAND;
    }

    /**
     * Encodes the message attributes to the output stream.
     *
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeInt(command.getType().ordinal());
        command.encode(stream);
    }

    /**
     * Decodes the message attributes from the input stream.
     *
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
        command = CommandFactory.create(CommandType.values()[stream.readInt()]);
        command.decode(stream);
    }

    /**
     * Returns the command to be executed.
     *
     * @return the command
     */
    public ICommand getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "GAME_COMMAND {" +
                "command=" + command.getType() +
                '}';
    }
}
