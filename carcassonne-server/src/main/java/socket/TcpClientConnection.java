package socket;

import socket.events.TcpClientReadHandler;

import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousSocketChannel;

public class TcpClientConnection {
    private final AsynchronousSocketChannel channel;

    public TcpClientConnection(AsynchronousSocketChannel channel) {
        this.channel = channel;
        this.channel.read(null, this, new TcpClientReadHandler());
    }
}
