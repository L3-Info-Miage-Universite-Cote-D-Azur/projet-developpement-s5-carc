package client.network.socket.handler;

import client.network.socket.ITcpClientSocketListener;

import java.nio.channels.CompletionHandler;

/**
 * Handler for TCP connect.
 */
public class TcpConnectHandler implements CompletionHandler<Void, ITcpClientSocketListener> {
    /**
     * Invoked when the connection is established.
     * @param result
     * @param listener The listener of the socket.
     */
    @Override
    public void completed(Void result, ITcpClientSocketListener listener) {
        listener.onConnected();
    }

    /**
     * Invoked when an error occurs.
     * @param exc The exception.
     * @param listener The listener of the socket.
     */
    @Override
    public void failed(Throwable exc, ITcpClientSocketListener listener) {
        listener.onConnectFailed();
    }
}
