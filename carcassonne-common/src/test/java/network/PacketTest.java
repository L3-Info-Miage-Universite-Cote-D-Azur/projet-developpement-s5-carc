package network;

import network.message.game.GameResultMessage;
import org.junit.jupiter.api.Test;
import stream.ByteInputStream;
import stream.ByteOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PacketTest {
    @Test
    public void testEncodeDecode() {
        Packet originalPacket = Packet.create(new GameResultMessage(new byte[]{1, 2, 3, 4, 5}));
        ByteOutputStream outputStream = new ByteOutputStream(50);
        originalPacket.encode(outputStream);
        ByteInputStream inputStream = new ByteInputStream(outputStream.getBytes(), outputStream.getLength());
        Packet decodedPacket = new Packet();
        assertTrue(decodedPacket.decode(inputStream) >= 0);
        assertEquals(originalPacket.getMessageType(), decodedPacket.getMessageType());
        assertEquals(originalPacket.getChecksum(), decodedPacket.getChecksum());
        assertEquals(originalPacket.getMessageLength(), decodedPacket.getMessageLength());

        for (int i = 0; i < originalPacket.getMessageLength(); i++) {
            assertEquals(originalPacket.getMessageData()[i], decodedPacket.getMessageData()[i]);
        }
    }

    @Test
    public void testDetectCorruptedMessageData() {
        Packet originalPacket = Packet.create(new GameResultMessage(new byte[]{1, 2, 3, 4, 5}));
        ByteOutputStream outputStream = new ByteOutputStream(50);
        originalPacket.encode(outputStream);
        outputStream.getBytes()[Packet.HEADER_SIZE - 4] ^= (byte) 0xFF;
        ByteInputStream inputStream = new ByteInputStream(outputStream.getBytes(), outputStream.getLength());
        Packet decodedPacket = new Packet();
        assertEquals(-3, decodedPacket.decode(inputStream));
    }

    @Test
    public void testDetectInvalidHeader() {
        Packet originalPacket = Packet.create(new GameResultMessage(new byte[]{1, 2, 3, 4, 5}));
        ByteOutputStream outputStream = new ByteOutputStream(50);
        originalPacket.encode(outputStream);
        outputStream.getBytes()[0] ^= (byte) 0xFF;
        ByteInputStream inputStream = new ByteInputStream(outputStream.getBytes(), outputStream.getLength());
        Packet decodedPacket = new Packet();
        assertEquals(-2, decodedPacket.decode(inputStream));
    }

    @Test
    public void testDetectInvalidTrailer() {
        Packet originalPacket = Packet.create(new GameResultMessage(new byte[]{1, 2, 3, 4, 5}));
        ByteOutputStream outputStream = new ByteOutputStream(50);
        originalPacket.encode(outputStream);
        outputStream.getBytes()[outputStream.getLength() - 1] ^= (byte) 0xFF;
        ByteInputStream inputStream = new ByteInputStream(outputStream.getBytes(), outputStream.getLength());
        Packet decodedPacket = new Packet();
        assertEquals(-2, decodedPacket.decode(inputStream));
    }

    @Test
    public void testDeserializeMessageFromStream() {
        Packet originalPacket = Packet.create(new GameResultMessage(new byte[]{1, 2, 3, 4, 5}));
        ByteOutputStream outputStream = new ByteOutputStream(50);
        originalPacket.encode(outputStream);
        ByteInputStream inputStream = new ByteInputStream(outputStream.getBytes(), outputStream.getLength());
        Packet decodedPacket = new Packet();
        assertTrue(decodedPacket.decode(inputStream) >= 0);
        assertEquals(GameResultMessage.class, decodedPacket.getMessage().getClass());
    }
}
