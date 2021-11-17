package network.socket.handler;

import network.socket.ITcpClientSocketListener;

import java.nio.channels.CompletionHandler;

public class TcpReadHandler implements CompletionHandler<Integer, ITcpClientSocketListener> {
    @Override
    public void completed(Integer result, ITcpClientSocketListener listener) {
        if (result == -1) {
            listener.onDisconnected();
        } else {
            listener.onReceive(result.intValue());
        }
    }

    @Override
    public void failed(Throwable exc, ITcpClientSocketListener listener) {
        listener.onDisconnected();
    }
}
