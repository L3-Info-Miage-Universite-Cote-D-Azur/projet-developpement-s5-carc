package server.network.socket.handler;

import server.network.ClientConnection;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Handler for reading data from a client.
 */
public class TcpReadHandler implements CompletionHandler<Integer, ClientConnection> {
    private final AsynchronousSocketChannel channel;
    private final ByteBuffer buffer;

    public TcpReadHandler(AsynchronousSocketChannel channel, ByteBuffer buffer) {
        this.channel = channel;
        this.buffer = buffer;
    }

    /**
     * Invoked when the data has been read.
     *
     * @param read       Bytes The number of bytes read.
     * @param connection The connection.
     */
    @Override
    public void completed(Integer read, ClientConnection connection) {
        if (read == -1) {
            connection.close();
        } else {
            connection.onReceive(read.intValue());

            if (connection.isConnected()) {
                channel.read(buffer, connection, this);
            }
        }
    }

    /**
     * Invoked when an error occurred.
     *
     * @param exc        The error.
     * @param connection The connection.
     */
    @Override
    public void failed(Throwable exc, ClientConnection connection) {
        connection.close();
    }
}
