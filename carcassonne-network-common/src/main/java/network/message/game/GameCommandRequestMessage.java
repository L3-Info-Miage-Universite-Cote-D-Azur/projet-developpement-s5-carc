package network.message.game;

import logic.command.CommandFactory;
import logic.command.CommandType;
import logic.command.ICommand;
import network.message.Message;
import network.message.MessageType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Message sent by the client to the server to request the execution of the specified command.
 */
public class GameCommandRequestMessage extends Message {
    private ICommand command;

    public GameCommandRequestMessage() {
    }

    public GameCommandRequestMessage(ICommand command) {
        this.command = command;
    }

    /**
     * Returns the message type.
     * @return the message type
     */
    @Override
    public MessageType getType() {
        return MessageType.GAME_COMMAND_REQUEST;
    }

    /**
     * Encodes the message attributes to the output stream.
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeInt(command.getType().getValue());
        command.encode(stream);
    }

    /**
     * Decodes the message attributes from the input stream.
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
        command = CommandFactory.create(CommandType.getCommandType(stream.readInt()));
        command.decode(stream);
    }

    /**
     * Returns the command to execute.
     * @return the command
     */
    public ICommand getCommand() {
        return command;
    }
}
