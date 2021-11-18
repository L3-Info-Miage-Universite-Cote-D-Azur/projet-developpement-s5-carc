package server.network.socket;

import server.network.socket.handler.TcpAcceptHandler;

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
        serverSocketChannel.accept(null, new TcpAcceptHandler(serverSocketChannel));
    }

    public void stop()  {
        try {
            serverSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
