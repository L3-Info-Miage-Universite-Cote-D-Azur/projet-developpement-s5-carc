package network.socket.handler;

import network.socket.ITcpClientSocketListener;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Handler for reading data from a TCP socket.
 */
public class TcpReadHandler implements CompletionHandler<Integer, ITcpClientSocketListener> {
    private final AsynchronousSocketChannel channel;
    private final ByteBuffer buffer;

    public TcpReadHandler(AsynchronousSocketChannel channel, ByteBuffer buffer) {
        this.channel = channel;
        this.buffer = buffer;
    }

    /**
     * Invoked when a read operation completes.
     * @param result The number of bytes read, possibly zero.
     * @param listener The listener.
     */
    @Override
    public void completed(Integer result, ITcpClientSocketListener listener) {
        if (result == -1) {
            listener.onDisconnected();
        } else {
            listener.onReceive(result.intValue());

            if (channel.isOpen()) {
                channel.read(buffer, listener, this);
            }
        }
    }

    /**
     * Invoked when an operation fails.
     * @param exc The exception.
     * @param listener The listener.
     */
    @Override
    public void failed(Throwable exc, ITcpClientSocketListener listener) {
        listener.onDisconnected();
    }
}
