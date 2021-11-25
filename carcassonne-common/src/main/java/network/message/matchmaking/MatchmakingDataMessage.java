package network.message.matchmaking;

import network.message.Message;
import network.message.MessageType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Message sent by the server to the client to inform the client of the matchmaking status.
 * It contains the current number of players in the matchmaking and the number of players
 * required to start the game.
 */
public class MatchmakingDataMessage extends Message {
    private int numPlayers;
    private int requiredPlayers;

    public MatchmakingDataMessage() {

    }

    public MatchmakingDataMessage(int numPlayers, int requiredPlayers) {
        this.numPlayers = numPlayers;
        this.requiredPlayers = requiredPlayers;
    }

    /**
     * Returns the message type.
     *
     * @return the message type
     */
    @Override
    public MessageType getType() {
        return MessageType.MATCHMAKING_DATA;
    }

    /**
     * Encodes the message attributes to the output stream.
     *
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeInt(numPlayers);
        stream.writeInt(requiredPlayers);
    }

    /**
     * Decodes the message attributes from the input stream.
     *
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
        numPlayers = stream.readInt();
        requiredPlayers = stream.readInt();
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getRequiredPlayers() {
        return requiredPlayers;
    }

    @Override
    public String toString() {
        return "MATCHMAKING_DATA {" +
                "numPlayers=" + numPlayers +
                ", requiredPlayers=" + requiredPlayers +
                '}';
    }
}
