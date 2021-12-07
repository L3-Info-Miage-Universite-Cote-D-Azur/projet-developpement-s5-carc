package server;

import java.io.IOException;
import java.net.Socket;

public class SimpleTcpClient {
    private final Socket socket;

    public SimpleTcpClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
    }

    public void send(String message) throws IOException {
        socket.getOutputStream().write(message.getBytes());
    }

    public byte[] receive() throws IOException {
        byte[] buffer = new byte[1024];
        int read = socket.getInputStream().read(buffer);
        byte[] result = new byte[read];
        System.arraycopy(buffer, 0, result, 0, read);
        return result;
    }

    public boolean isConnected() {
        return socket.isConnected();
    }
}
