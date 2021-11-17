package socket.events;

import socket.TcpClientConnection;

import java.nio.channels.CompletionHandler;

public class TcpClientReadHandler implements CompletionHandler<Integer, TcpClientConnection> {
    @Override
    public void completed(Integer result, TcpClientConnection attachment) {

    }

    @Override
    public void failed(Throwable exc, TcpClientConnection attachment) {

    }
}
