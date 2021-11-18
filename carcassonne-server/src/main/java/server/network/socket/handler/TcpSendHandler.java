package server.network.socket.handler;

import server.network.ClientConnection;

import java.nio.channels.CompletionHandler;

public class TcpSendHandler implements CompletionHandler<Integer, ClientConnection> {
    /**
     * Invoked when the data has been transferred.
     * @param result the number of bytes that were transferred
     * @param connection the connection
     */
    @Override
    public void completed(Integer result, ClientConnection connection) {
        if (result == -1) {
            connection.close();
        } else {
            connection.onSend(result.intValue());
        }
    }

    @Override
    public void failed(Throwable exc, ClientConnection connection) {
        connection.close();
    }
}
