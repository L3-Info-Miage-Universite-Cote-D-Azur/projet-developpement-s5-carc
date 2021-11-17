package network.socket.handler;

import network.socket.ITcpClientSocketListener;

import java.nio.channels.CompletionHandler;

public class TcpSendHandler implements CompletionHandler<Integer, ITcpClientSocketListener> {
    @Override
    public void completed(Integer result, ITcpClientSocketListener listener) {
        if (result == -1) {
            listener.onDisconnected();
        }
    }

    @Override
    public void failed(Throwable exc, ITcpClientSocketListener listener) {
        listener.onDisconnected();
    }
}
