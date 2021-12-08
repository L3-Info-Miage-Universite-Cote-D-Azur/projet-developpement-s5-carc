package network.message;

import logic.command.PlaceTileDrawnCommand;
import logic.math.Vector2;
import network.message.connection.ClientHelloMessage;
import network.message.connection.ServerHelloMessage;
import network.message.game.*;
import network.message.matchmaking.*;
import org.junit.jupiter.api.Test;
import stream.ByteInputStream;
import stream.ByteOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    @Test
    public void testAllMessagesHaveDefaultConstructor() throws NoSuchMethodException {
        for (MessageType type : MessageType.values()) {
            assertNotNull(type.getMessageClass().getDeclaredConstructor());
        }
    }

    @Test
    public void testFactoryReturnsCorrectMessage() {
        for (MessageType type : MessageType.values()) {
            Message message = MessageFactory.create(type);
            assertNotNull(message);
            assertEquals(type, message.getType());
            assertEquals(type.getMessageClass(), message.getClass());
        }
    }

    @Test
    public void testClientHelloEncodingDecoding() {
        ClientHelloMessage original = new ClientHelloMessage();
        ClientHelloMessage decoded = cloneUsingEncodeDecode(original);
    }

    @Test
    public void testServerHelloEncodingDecoding() {
        ServerHelloMessage original = new ServerHelloMessage(12345);
        ServerHelloMessage decoded = cloneUsingEncodeDecode(original);

        assertEquals(original.getUserId(), decoded.getUserId());
    }

    @Test
    public void testGameCommandEncodingDecoding() {
        GameCommandMessage original = new GameCommandMessage(new PlaceTileDrawnCommand(new Vector2(1, 2)));
        GameCommandMessage decoded = cloneUsingEncodeDecode(original);

        assertEquals(original.getCommand().getType(), decoded.getCommand().getType());
    }

    @Test
    public void testGameCommandRequestEncodingDecoding() {
        GameCommandRequestMessage original = new GameCommandRequestMessage(new PlaceTileDrawnCommand(new Vector2(1, 2)));
        GameCommandRequestMessage decoded = cloneUsingEncodeDecode(original);

        assertEquals(original.getCommand().getType(), decoded.getCommand().getType());
    }

    @Test
    public void testGameDataEncodingDecoding() {
        GameDataMessage original = new GameDataMessage(new byte[] { 1, 2, 3, 4, 5 });
        GameDataMessage decoded = cloneUsingEncodeDecode(original);

        assertArrayEquals(original.getData(), decoded.getData());
    }

    @Test
    public void testGameMasterNextTurnDataEncodingDecoding() {
        GameMasterNextTurnDataMessage original = new GameMasterNextTurnDataMessage(-1);
        GameMasterNextTurnDataMessage decoded = cloneUsingEncodeDecode(original);

        assertEquals(original.getTileConfigIndex(), decoded.getTileConfigIndex());
    }

    @Test
    public void testGameResultEncodingDecoding() {
        GameResultMessage original = new GameResultMessage(new byte[] { 1, 2, 3, 4, 5 });
        GameResultMessage decoded = cloneUsingEncodeDecode(original);

        assertArrayEquals(original.getData(), decoded.getData());
    }

    @Test
    public void testJoinMatchmakingEncodingDecoding() {
        JoinMatchmakingMessage original = new JoinMatchmakingMessage(12345);
        JoinMatchmakingMessage decoded = cloneUsingEncodeDecode(original);

        assertEquals(original.getMatchCapacity(), decoded.getMatchCapacity());
    }

    @Test
    public void testLeaveMatchmakingEncodingDecoding() {
        LeaveMatchmakingMessage original = new LeaveMatchmakingMessage();
        LeaveMatchmakingMessage decoded = cloneUsingEncodeDecode(original);
    }

    @Test
    public void testMatchmakingDataEncodingDecoding() {
        MatchmakingDataMessage original = new MatchmakingDataMessage(12345, 67890);
        MatchmakingDataMessage decoded = cloneUsingEncodeDecode(original);

        assertEquals(original.getNumPlayers(), decoded.getNumPlayers());
        assertEquals(original.getRequiredPlayers(), decoded.getRequiredPlayers());
    }

    @Test
    public void testMatchmakingFailedEncodingDecoding() {
        MatchmakingFailedMessage original = new MatchmakingFailedMessage();
        MatchmakingFailedMessage decoded = cloneUsingEncodeDecode(original);
    }

    @Test
    public void testMatchmakingLeftEncodingDecoding() {
        MatchmakingLeftMessage original = new MatchmakingLeftMessage();
        MatchmakingLeftMessage decoded = cloneUsingEncodeDecode(original);
    }

    private static <E extends Message> E cloneUsingEncodeDecode(E message) {
        ByteOutputStream out = new ByteOutputStream(100);
        message.encode(out);

        E decoded = (E) MessageFactory.create(message.getType());
        ByteInputStream in = new ByteInputStream(out.getBytes(), out.getLength());
        decoded.decode(in);
        assertTrue(in.isAtEnd());

        return decoded;
    }
}