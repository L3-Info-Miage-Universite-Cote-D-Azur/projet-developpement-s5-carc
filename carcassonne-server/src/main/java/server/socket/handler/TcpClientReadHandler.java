package server.socket.handler;

import server.socket.TcpClientConnection;

import java.nio.channels.CompletionHandler;

public class TcpClientReadHandler implements CompletionHandler<Integer, TcpClientConnection> {
    @Override
    public void completed(Integer read, TcpClientConnection connection) {
        if (read == -1) {
            connection.close();
        } else {
            connection.onRead(read.intValue());
        }
    }

    @Override
    public void failed(Throwable exc, TcpClientConnection connection) {
        connection.close();
    }
}
