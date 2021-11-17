package socket.events;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class TcpServerAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {
    private AsynchronousServerSocketChannel serverSocketChannel;

    public TcpServerAcceptHandler(AsynchronousServerSocketChannel serverSocketChannel) {
        this.serverSocketChannel = serverSocketChannel;
    }

    @Override
    public void completed(AsynchronousSocketChannel clientSocketChannel, Object attachment) {
        if (serverSocketChannel.isOpen()) {
            serverSocketChannel.accept(null, this);
        }


        if (clientSocketChannel != null && clientSocketChannel.isOpen()) {

        }
    }

    @Override
    public void failed(Throwable exc, Object attachment) {

    }
}
