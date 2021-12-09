package client.network;

import client.message.MessageDispatcher;
import network.Packet;
import network.message.IMessage;
import network.message.connection.ClientHelloMessage;
import network.message.connection.ServerHelloMessage;
import org.junit.jupiter.api.Test;
import reflection.ReflectionUtils;
import stream.ByteOutputStream;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerConnectionTest {
    private static byte[] getPacketBytes(Packet packet) {
        ByteOutputStream stream = new ByteOutputStream(64);
        packet.encode(stream);
        return stream.toByteArray();
    }

    @Test
    void testOnRead() throws Exception {
        final Boolean[] hasReceivedMessage = {false, false};
        final Boolean[] hasClosed = {false};

        final int[] receivedMessageCount = {0};

        ServerConnection serverConnection = new ServerConnection() {
            @Override
            public void close() {
                hasClosed[0] = true;
            }
        };

        ReflectionUtils.setField(serverConnection, "messageDispatcher", new MessageDispatcher() {
            @Override
            public void handle(IMessage message) {
                hasReceivedMessage[receivedMessageCount[0]++] = true;
            }
        });

        ByteBuffer readBuffer = (ByteBuffer) ReflectionUtils.getField(serverConnection, "readBuffer");

        readBuffer.put(getPacketBytes(Packet.create(new ClientHelloMessage())));
        readBuffer.put(getPacketBytes(Packet.create(new ServerHelloMessage())));

        serverConnection.onReceive(readBuffer.position());

        assertTrue(hasReceivedMessage[0]);
        assertTrue(hasReceivedMessage[1]);
        assertFalse(hasClosed[0]);

        serverConnection.onReceive(readBuffer.position());

        // Put wrong data to check if the connection is closed
        readBuffer.put(new byte[32]);

        serverConnection.onReceive(readBuffer.position());

        assertTrue(hasClosed[0]);
    }
}
