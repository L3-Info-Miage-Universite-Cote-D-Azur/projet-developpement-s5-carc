package server.network;

import network.Packet;
import network.message.IMessage;
import network.message.connection.ClientHelloMessage;
import network.message.connection.ServerHelloMessage;
import network.message.game.GameResultMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reflection.ReflectionUtils;
import server.message.MessageHandler;
import stream.ByteOutputStream;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

class ClientConnectionTest {
    private ClientConnection clientConnection;
    private ByteBuffer receiveBuffer;

    private boolean isClosedCalled;
    private int receivedMessageCount;

    private static byte[] getPacketBytes(Packet packet) {
        ByteOutputStream stream = new ByteOutputStream(64);
        packet.encode(stream);
        return stream.toByteArray();
    }

    @BeforeEach
    void setup() throws Exception {
        clientConnection = new ClientConnection(null, 1) {
            @Override
            public void close() {
                isClosedCalled = true;
            }
        };

        ReflectionUtils.setField(clientConnection, "messageHandler", new MessageHandler(clientConnection) {
            @Override
            public void handle(IMessage message) {
                receivedMessageCount++;
            }
        });

        receiveBuffer = (ByteBuffer) ReflectionUtils.getField(clientConnection, "receiveBuffer");
    }

    @Test
    void testMessageHandling() {
        receiveBuffer.put(getPacketBytes(Packet.create(new ClientHelloMessage())));
        receiveBuffer.put(getPacketBytes(Packet.create(new ServerHelloMessage())));

        clientConnection.onReceive(receiveBuffer.position());

        assertEquals(2, receivedMessageCount);
    }

    @Test
    void testCloseOnBadChecksum() {
        receiveBuffer.put(getPacketBytes(Packet.create(new ClientHelloMessage())));
        receiveBuffer.array()[8] = (byte) 0xFF;
        receiveBuffer.array()[9] = (byte) 0xFF;
        receiveBuffer.array()[10] = (byte) 0xFF;
        receiveBuffer.array()[11] = (byte) 0xFF;

        clientConnection.onReceive(receiveBuffer.position());

        assertTrue(isClosedCalled);
    }

    @Test
    void testCloseOnBadHeader() {
        receiveBuffer.put(getPacketBytes(Packet.create(new ClientHelloMessage())));
        receiveBuffer.array()[0] = (byte) 0xFF;
        receiveBuffer.array()[1] = (byte) 0xFF;
        receiveBuffer.array()[2] = (byte) 0xFF;
        receiveBuffer.array()[3] = (byte) 0xFF;

        clientConnection.onReceive(receiveBuffer.position());

        assertTrue(isClosedCalled);
    }

    @Test
    void testCloseOnTooBigMessage() {
        byte[] data = getPacketBytes(Packet.create(new GameResultMessage(new byte[100000])));

        // No need to write the whole message, just write the header
        receiveBuffer.put(data, 0, 20);
        clientConnection.onReceive(20);

        assertTrue(isClosedCalled);
    }

    @Test
    void testDontHandleWhenMessageNotCompletelyReceived() {
        receiveBuffer.put(getPacketBytes(Packet.create(new ClientHelloMessage())));

        clientConnection.onReceive(receiveBuffer.position() - 1);

        assertEquals(0, receivedMessageCount);
        assertFalse(isClosedCalled);
    }

    @Test
    void testHandleFirstMessageWhenSecondNotCompletelyReceived() {
        receiveBuffer.put(getPacketBytes(Packet.create(new ClientHelloMessage())));
        receiveBuffer.put(getPacketBytes(Packet.create(new ServerHelloMessage())));

        clientConnection.onReceive(receiveBuffer.position() - 1);

        assertEquals(1, receivedMessageCount);
        assertFalse(isClosedCalled);
    }
}
