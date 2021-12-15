package server.network;

import network.Packet;
import network.ResizableByteBuffer;
import network.message.IMessage;
import server.Server;
import server.logger.Logger;
import server.message.MessageHandler;
import server.network.socket.handler.TcpReadHandler;
import server.network.socket.handler.TcpSendHandler;
import server.session.ClientSession;
import stream.ByteInputStream;
import stream.ByteOutputStream;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.time.LocalDateTime;

/**
 * Represents a TCP client connection.
 */
public class ClientConnection {
    /**
     * Size of the buffer used to read data from the socket.
     * When the data is read, it'll be stored in the stream receiveStream.
     * If the buffer is full, extra data is handled by the next call to onReceive.
     */
    private static final int READ_BUFFER_SIZE = 1024;

    /**
     * Initial size of the stream used to store data received from socket and to be handled.
     */
    private static final int INITIAL_RECEIVE_STREAM_SIZE = 1024;

    /**
     * Max size of the stream used to store data received from socket and to be handled.
     * If the stream is full, the connection is closed.
     */
    private static final int MAX_RECEIVE_BUFFER_SIZE = 1024 * 1024;

    /**
     * Timeout before closing the connection if no data is received.
     */
    private static final int LAST_PACKET_TIMEOUT = 60000;

    /**
     * Initial size of the stream used to store data to send to the socket.
     */
    private static final int INITIAL_SEND_STREAM_SIZE = 1024;

    /**
     * Max size of the stream used to store data to send to the socket.
     * If the stream is full, the connection is closed.
     */
    private static final int MAX_SEND_BUFFER_SIZE = 1024 * 1024;

    /**
     * The socket channel used to communicate with the client.
     */
    private final AsynchronousSocketChannel channel;

    /**
     * The handler used to handle the received messages.
     */
    private final MessageHandler messageHandler;

    /**
     * The buffer used to store data received from the socket.
     */
    private final ByteBuffer receiveBuffer;

    /**
     * The stream used to store data to be handled.
     */
    private final ResizableByteBuffer receiveStream;

    /**
     * The stream used to store data to be sent to the socket.
     */
    private final ResizableByteBuffer sendStream;

    /**
     * The connection's id.
     */
    private final int id;

    /**
     * Session data of the client.
     */
    private ClientSession session;

    /**
     * The last time the connection was received data.
     */
    private LocalDateTime lastRead;


    /**
     * Whether the connection is destroyed.
     */
    private volatile boolean destroyed;

    public ClientConnection(AsynchronousSocketChannel channel, int id) {
        this.channel = channel;
        this.id = id;
        this.messageHandler = new MessageHandler(this);
        this.receiveBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE);
        this.receiveStream = new ResizableByteBuffer(INITIAL_RECEIVE_STREAM_SIZE, MAX_RECEIVE_BUFFER_SIZE);
        this.sendStream = new ResizableByteBuffer(INITIAL_SEND_STREAM_SIZE, MAX_SEND_BUFFER_SIZE);
        this.lastRead = LocalDateTime.now();
    }

    public void startIO() {
        this.channel.read(receiveBuffer, this, new TcpReadHandler(this.channel, this.receiveBuffer));
    }

    /**
     * Gets the id of the connection.
     *
     * @return The id of the connection.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the session of the client.
     *
     * @return The session of the client.
     */
    public ClientSession getSession() {
        return session;
    }

    /**
     * Sets the session of the client.
     *
     * @param session The session of the client.
     */
    public void setSession(ClientSession session) {
        this.session = session;
    }

    /**
     * Gets the remote address of the connection.
     *
     * @return The remote address of the connection.
     */
    public String getRemoteAddress() {
        try {
            return channel.getRemoteAddress().toString();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Closes the connection.
     */
    public synchronized void close() {
        synchronized (this) {
            if (destroyed) {
                return;
            }

            destroyed = true;
        }

        if (session != null) {
            session.destroy();
        }

        try {
            channel.close();
            receiveStream.clear();
            receiveBuffer.clear();
        } catch (Exception e) {
            // Ignore
        } finally {
            Server.getInstance().getConnectionManager().removeConnection(this);
        }
    }

    /**
     * Called when data is received.
     *
     * @param length The length of the data received.
     */
    public synchronized void onReceive(int length) {
        receiveBuffer.position(length);
        receiveBuffer.flip();
        receiveStream.put(receiveBuffer.array(), 0, length);
        receiveBuffer.clear();

        ByteInputStream stream = new ByteInputStream(receiveStream.getBuffer(), receiveStream.size());
        int bytesRead = 0;

        while (!stream.isAtEnd()) {
            Packet packet = new Packet();
            int read = packet.decode(stream);

            if (read == -1) {
                break;
            } else if (read == -2) {
                Logger.warn("Connection %d: Packet header invalid, closing connection.", id);
                close();
                return;
            } else if (read == -3) {
                Logger.warn("Connection %d: Packet checksum mismatch, closing connection.", id);
                close();
                return;
            }

            Logger.debug("Connection %d: Received message %s", id, packet.getMessage());

            bytesRead += read;
            messageHandler.handle(packet.getMessage());
        }

        receiveStream.remove(bytesRead);
        lastRead = LocalDateTime.now();
    }

    /**
     * Invoked when the connection was sent data.
     *
     * @param length
     */
    public synchronized void onSend(int length) {
        sendStream.remove(length);

        if (sendStream.size() != 0) {
            channel.write(ByteBuffer.wrap(sendStream.getBuffer(), 0, sendStream.size()), this, new TcpSendHandler());
        }
    }

    /**
     * Sends the given message to the client.
     *
     * @param message The message to send.
     */
    public synchronized void send(IMessage message) {
        Logger.debug("Connection %d: Sending message %s", id, message);

        Packet packet = Packet.create(message);
        ByteOutputStream stream = new ByteOutputStream(64);
        packet.encode(stream);

        send(stream.getBytes(), 0, stream.getLength());
    }

    /**
     * Sends the given data to the client.
     *
     * @param buffer The data to send.
     */
    private synchronized void send(byte[] buffer, int offset, int length) {
        if (sendStream.size() == 0) {
            sendStream.put(buffer, offset, length);
            channel.write(ByteBuffer.wrap(sendStream.getBuffer(), 0, sendStream.size()), this, new TcpSendHandler());
        } else {
            sendStream.put(buffer, offset, length);
        }
    }

    /**
     * Gets if the connection is alive.
     *
     * @return True if the connection is alive, false otherwise.
     */
    public boolean isConnected() {
        return channel.isOpen() && lastRead.isAfter(LocalDateTime.now().minusSeconds(LAST_PACKET_TIMEOUT));
    }
}
