package server.socket;

import network.Packet;
import network.message.Message;
import server.Server;
import server.message.MessageHandler;
import server.socket.handler.TcpClientReadHandler;
import stream.ByteInputStream;
import stream.ByteOutputStream;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.time.LocalDateTime;

/**
 * Represents a TCP client connection.
 */
public class TcpClientConnection {
    private static final int READ_BUFFER_SIZE = 1024;
    private static final int WRITE_BUFFER_SIZE = 1024;
    private static final int LAST_PACKET_TIMEOUT = 60000;

    private final AsynchronousSocketChannel channel;
    private final MessageHandler messageHandler;
    private final ByteBuffer readBuffer;
    private final ByteBuffer writeBuffer;

    private final int id;

    private LocalDateTime lastRead;

    public TcpClientConnection(AsynchronousSocketChannel channel, int id) {
        this.channel = channel;
        this.id = id;
        this.messageHandler = new MessageHandler(this);
        this.readBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE);
        this.writeBuffer = ByteBuffer.allocate(WRITE_BUFFER_SIZE);
        this.channel.read(readBuffer, this, new TcpClientReadHandler());

        this.lastRead = LocalDateTime.now();
    }

    /**
     * Gets the id of the connection.
     * @return The id of the connection.
     */
    public int getId() {
        return id;
    }

    public String getRemoteAddress() {
        try {
            return channel.getRemoteAddress().toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Closes the connection.
     */
    public void close() {
        try {
            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readBuffer.clear();
            writeBuffer.clear();

            Server.getInstance().getConnectionManager().removeConnection(this);
        }
    }

    /**
     * Called when data is received.
     * @param length The length of the data received.
     */
    public void onRead(int length) {
        readBuffer.position(length);
        readBuffer.flip();

        ByteInputStream inputStream = new ByteInputStream(readBuffer.array(), length);

        while (!inputStream.isAtEnd()) {
            Packet packet = new Packet();
            int packetLength = packet.decode(inputStream);

            if (packetLength == -1) {
                break;
            } else if (packetLength == -2) {
                System.out.println("Invalid packet received.");
                close();
                return;
            } else if (packetLength == -3) {
                System.out.println("Checksum mismatch.");
                close();
                return;
            }

            readBuffer.position(readBuffer.position() + packetLength);
            messageHandler.handle(packet.getMessage());
        }

        lastRead = LocalDateTime.now();
    }

    public void send(Message message) {
        Packet packet = Packet.create(message);
        ByteOutputStream stream = new ByteOutputStream(64);
        packet.encode(stream);

        writeBuffer.clear();
        writeBuffer.put(stream.getBytes(), 0, stream.getLength());
        writeBuffer.flip();

        channel.write(writeBuffer, this, null);
    }

    public boolean isConnected() {
        return channel.isOpen() && lastRead.isAfter(LocalDateTime.now().minusSeconds(LAST_PACKET_TIMEOUT));
    }
}
