package network.message;

import network.message.connection.*;
import network.message.game.*;
import network.message.matchmaking.*;

/**
 * MessageId is an enumeration of all the network.message types.
 */
public enum MessageId {
    CLIENT_HELLO(100, ClientHelloMessage.class),
    SERVER_HELLO(200, ServerHelloMessage.class),
    JOIN_MATCHMAKING(110, JoinMatchmakingMessage.class),
    MATCHMAKING_DATA(111, MatchmakingDataMessage.class),
    LEAVE_MATCHMAKING(120, LeaveMatchmakingMessage.class),
    MATCHMAKING_LEFT(121, MatchmakingLeftMessage.class),

    GAME_DATA(210, GameDataMessage.class),
    GAME_COMMAND_REQUEST(111, GameCommandRequestMessage.class),
    GAME_COMMAND(211, GameCommandMessage.class),
    GAME_RESULT(212, GameResultMessage.class);

    private final int id;
    private final Class<? extends Message> messageClass;

    MessageId(int id, Class<? extends Message> messageClass) {
        this.id = id;
        this.messageClass = messageClass;
    }

    public int getId() {
        return id;
    }

    public Class<? extends Message> getMessageClass() {
        return messageClass;
    }

    public static MessageId getById(int id) {
        for (MessageId messageId : MessageId.values()) {
            if (messageId.getId() == id) {
                return messageId;
            }
        }

        return null;
    }
}
