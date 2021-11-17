package network.socket.handler;

import network.socket.ITcpClientSocketListener;

import java.nio.channels.CompletionHandler;

public class TcpConnectHandler implements CompletionHandler<Void, ITcpClientSocketListener> {
    @Override
    public void completed(Void result, ITcpClientSocketListener listener) {
        listener.onConnected();
    }

    @Override
    public void failed(Throwable exc, ITcpClientSocketListener listener) {
        listener.onConnectFailed();
    }
}
