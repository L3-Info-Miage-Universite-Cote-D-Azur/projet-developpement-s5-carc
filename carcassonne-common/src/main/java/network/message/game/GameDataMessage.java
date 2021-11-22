package network.message.game;

import network.message.Message;
import network.message.MessageType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Message sent to the client to send the current game data.
 * It's used to synchronize the client game data with the server game data.
 */
public class GameDataMessage extends Message {
    /**
     * Serialized game data.
     */
    private byte[] data;

    public GameDataMessage() {
    }

    public GameDataMessage(byte[] data) {
        this.data = data;
    }

    /**
     * Returns the message type.
     * @return the message type
     */
    @Override
    public MessageType getType() {
        return MessageType.GAME_DATA;
    }

    /**
     * Encodes the message attributes to the output stream.
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeBytes(data);
    }

    /**
     * Decodes the message attributes from the input stream.
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
        data = stream.readBytes();
    }

    /**
     * Returns the serialized game slave data.
     * @return the serialized game slave data
     */
    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "GAME_DATA {}";
    }
}
