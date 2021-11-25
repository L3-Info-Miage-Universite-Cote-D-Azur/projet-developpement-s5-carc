package client.network.socket.handler;

import client.network.socket.ITcpClientSocketListener;

import java.nio.channels.CompletionHandler;

/**
 * Handler for sending data to the server.
 */
public class TcpSendHandler implements CompletionHandler<Integer, ITcpClientSocketListener> {
    /**
     * Called when the data has been sent.
     *
     * @param result   The number of bytes sent.
     * @param listener The listener.
     */
    @Override
    public void completed(Integer result, ITcpClientSocketListener listener) {
        if (result == -1) {
            listener.onDisconnected();
        } else {
            listener.onSend(result.intValue());
        }
    }

    /**
     * Called when an error occurs.
     *
     * @param exc      The exception.
     * @param listener The listener.
     */
    @Override
    public void failed(Throwable exc, ITcpClientSocketListener listener) {
        listener.onDisconnected();
    }
}
