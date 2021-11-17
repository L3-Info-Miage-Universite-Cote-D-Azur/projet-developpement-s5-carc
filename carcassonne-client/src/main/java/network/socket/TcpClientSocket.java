package network.socket;

import network.socket.handler.TcpConnectHandler;
import network.socket.handler.TcpReadHandler;
import network.socket.handler.TcpSendHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class TcpClientSocket {
    private final AsynchronousSocketChannel socketChannel;
    private ITcpClientSocketListener listener;

    public TcpClientSocket() throws IOException {
        socketChannel = AsynchronousSocketChannel.open();
    }

    public void close() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(String host, int port) {
        socketChannel.connect(new InetSocketAddress(host, port), listener, new TcpConnectHandler());
    }

    public void read(ByteBuffer buffer) {
        socketChannel.read(buffer, listener, new TcpReadHandler());
    }

    public void write(ByteBuffer buffer) {
        socketChannel.write(buffer, listener, new TcpSendHandler());
    }

    public void setListener(ITcpClientSocketListener listener) {
        this.listener = listener;
    }
}
