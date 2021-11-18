package network.socket;

/**
 * Interface for TCP client socket listeners.
 */
public interface ITcpClientSocketListener {
    /**
     * Called when the socket is connected.
     */
    void onConnected();

    /**
     * Called when the socket has failed to connect.
     */
    void onConnectFailed();

    /**
     * Called when the socket is disconnected.
     */
    void onDisconnected();

    /**
     * Called when the socket has received data.
     */
    void onReceive(int length);

    /**
     * Called when the socket was sent data.
     */
    void onSend(int length);
}
