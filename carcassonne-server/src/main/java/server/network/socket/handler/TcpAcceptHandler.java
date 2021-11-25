package server.network.socket.handler;

import server.Server;
import server.logger.Logger;
import server.network.ClientConnectionManager;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Handler for accepting new connections.
 */
public class TcpAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {
    private final AsynchronousServerSocketChannel channel;
    private final ClientConnectionManager connectionManager;

    public TcpAcceptHandler(AsynchronousServerSocketChannel channel) {
        this.channel = channel;
        this.connectionManager = Server.getInstance().getConnectionManager();
    }

    /**
     * Called when a new connection is accepted.
     *
     * @param clientSocketChannel The client socket channel.
     * @param attachment          The attachment.
     */
    @Override
    public void completed(AsynchronousSocketChannel clientSocketChannel, Object attachment) {
        try {
            if (clientSocketChannel != null && clientSocketChannel.isOpen()) {
                connectionManager.createConnection(clientSocketChannel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (channel.isOpen()) {
            channel.accept(null, this);
        }
    }

    /**
     * Called when an error occurs.
     *
     * @param exc        The throwable.
     * @param attachment The attachment.
     */
    @Override
    public void failed(Throwable exc, Object attachment) {
        Logger.error("Error accepting new connection: %s", exc.getMessage());
    }
}
