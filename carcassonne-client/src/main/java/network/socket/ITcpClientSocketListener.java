package network.socket;

public interface ITcpClientSocketListener {
    void onConnected();
    void onConnectFailed();
    void onDisconnected();
    void onReceive(int length);
}
