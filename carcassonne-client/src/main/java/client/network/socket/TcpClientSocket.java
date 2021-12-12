package client.network.socket;

import client.network.socket.handler.TcpConnectHandler;
import client.network.socket.handler.TcpReadHandler;
import client.network.socket.handler.TcpSendHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Represents a TCP client socket that can be used to send and receive data to and from a server.
 */
public class TcpClientSocket {
    /**
     * The socket channel used to send and receive data.
     */
    private final AsynchronousSocketChannel socketChannel;

    /**
     * The listener that is notified of socket events.
     */
    private ITcpClientSocketListener listener;

    public TcpClientSocket() throws IOException {
        socketChannel = AsynchronousSocketChannel.open();
    }

    /**
     * Closes the socket channel.
     */
    public void close() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            // Ignore
        }
    }

    /**
     * Connects to the specified host and port.
     *
     * @param serverAddress The address of the server to connect to.
     */
    public void connect(InetSocketAddress serverAddress) {
        socketChannel.connect(serverAddress, listener, new TcpConnectHandler());
    }

    /**
     * Reads data from the socket channel.
     *
     * @param buffer The buffer to read data into.
     */
    public void read(ByteBuffer buffer) {
        socketChannel.read(buffer, listener, new TcpReadHandler(socketChannel, buffer));
    }

    /**
     * Writes data to the socket channel.
     *
     * @param buffer The buffer to write data from.
     */
    public void write(ByteBuffer buffer) {
        socketChannel.write(buffer, listener, new TcpSendHandler());
    }

    /**
     * Sets the listener that is notified of socket events.
     *
     * @param listener The listener to notify.
     */
    public void setListener(ITcpClientSocketListener listener) {
        this.listener = listener;
    }
}
