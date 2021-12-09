package network.message.game;

import network.message.IMessage;
import network.message.MessageType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Message sent by the game master to the client to inform it of the next turn.
 */
public class GameMasterNextTurnDataMessage implements IMessage {
    private int tileConfigIndex;

    public GameMasterNextTurnDataMessage() {
        // ignored
    }

    public GameMasterNextTurnDataMessage(int tileConfigIndex) {
        this.tileConfigIndex = tileConfigIndex;
    }

    /**
     * Returns the message type.
     *
     * @return the message type
     */
    @Override
    public MessageType getType() {
        return MessageType.GAME_MASTER_NEXT_TURN_DATA;
    }

    /**
     * Encodes the message attributes to the output stream.
     *
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeInt(tileConfigIndex);
    }

    /**
     * Decodes the message attributes from the input stream.
     *
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
        tileConfigIndex = stream.readInt();
    }

    /**
     * Gets the tile configuration index.
     *
     * @return the tile configuration index
     */
    public int getTileConfigIndex() {
        return tileConfigIndex;
    }

    @Override
    public String toString() {
        return "GAME_MASTER_NEXT_TURN_DATA {" +
                "tileConfigIndex=" + tileConfigIndex +
                '}';
    }
}
