package client.network;

import client.message.MessageDispatcher;
import network.Packet;
import network.message.Message;
import network.message.connection.ClientHelloMessage;
import network.message.connection.ServerHelloMessage;
import org.junit.jupiter.api.Test;
import stream.ByteOutputStream;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

public class ServerConnectionTest {
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

        setPrivateField(serverConnection, "messageDispatcher", new MessageDispatcher(serverConnection) {
            @Override
            public void handle(Message message) {
                hasReceivedMessage[receivedMessageCount[0]++] = true;
            }
        });

        ByteBuffer readBuffer = (ByteBuffer) getPrivateField(serverConnection, "readBuffer");

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

    private static byte[] getPacketBytes(Packet packet) {
        ByteOutputStream stream = new ByteOutputStream(64);
        packet.encode(stream);
        return stream.toByteArray();
    }

    private static Object getPrivateField(Object obj, String fieldName) throws Exception {
        Field field = ServerConnection.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    private static void setPrivateField(Object obj, String fieldName, Object value) throws Exception {
        Field field = ServerConnection.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
}
