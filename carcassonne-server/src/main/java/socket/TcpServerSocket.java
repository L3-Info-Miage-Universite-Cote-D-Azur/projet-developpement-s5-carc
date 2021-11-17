package socket;

import socket.events.TcpServerAcceptHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

public class TcpServerSocket {
    private AsynchronousServerSocketChannel serverSocketChannel;

    public TcpServerSocket(String host, int port) throws IOException {
        serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(host, port));
    }

    public void run() {
        serverSocketChannel.accept(null, new TcpServerAcceptHandler(serverSocketChannel));
    }
}
