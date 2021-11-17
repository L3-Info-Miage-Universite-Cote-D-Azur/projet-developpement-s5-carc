package network.message.game;

import logic.command.CommandFactory;
import logic.command.CommandId;
import logic.command.ICommand;
import network.message.Message;
import network.message.MessageId;
import stream.ByteInputStream;
import stream.ByteOutputStream;

public class GameCommandRequestMessage extends Message {
    private ICommand command;

    public GameCommandRequestMessage() {
    }

    public GameCommandRequestMessage(ICommand command) {
        this.command = command;
    }

    @Override
    public MessageId getId() {
        return MessageId.GAME_COMMAND_REQUEST;
    }

    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeInt(command.getId());
        command.encode(stream);
    }

    @Override
    public void decode(ByteInputStream stream) {
        command = CommandFactory.create(CommandId.getCommandId(stream.readInt()));
        command.decode(stream);
    }
}
