package network.message.game;

import network.message.IMessage;
import network.message.MessageType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Message sent by the server to the client to inform the client of the result of the game.
 * It contains the winner of the game and the stats of the players.
 */
public class GameResultMessage implements IMessage {
    private byte[] data;

    public GameResultMessage() {
        // ignored
    }

    public GameResultMessage(byte[] data) {
        this.data = data;
    }

    @Override
    public MessageType getType() {
        return MessageType.GAME_RESULT;
    }

    /**
     * Encodes the message attributes to the output stream.
     *
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeBytes(data);
    }

    /**
     * Decodes the message attributes from the input stream.
     *
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
        data = stream.readBytes();
    }

    /**
     * Gets the serialized game master data.
     *
     * @return the serialized game master data
     */
    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "GAME_RESULT {}";
    }
}
