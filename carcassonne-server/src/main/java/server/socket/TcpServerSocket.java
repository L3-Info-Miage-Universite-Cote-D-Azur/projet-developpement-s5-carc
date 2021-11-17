package server.socket;

import server.socket.handler.TcpServerAcceptHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

public class TcpServerSocket {
    private AsynchronousServerSocketChannel serverSocketChannel;

    public TcpServerSocket(String host, int port) throws IOException {
        serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(host, port));
    }

    public void start() {
        serverSocketChannel.accept(null, new TcpServerAcceptHandler(serverSocketChannel));
    }

    public void stop()  {
        try {
            serverSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
