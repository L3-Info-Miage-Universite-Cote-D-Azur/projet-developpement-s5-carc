package client.network.socket;

import excel.ExcelNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

import static org.junit.jupiter.api.Assertions.*;

public class TcpClientSocketTest {
    private static final int ECHO_SERVER_PORT = 55555;
    private static Semaphore semaphore = new Semaphore(0);

    @Test
    void testConnectionToServer() throws IOException, InterruptedException {
        final boolean[] connected = {false};

        Object connectedLock = new Object();
        EchoServer echoServer = new EchoServer() {
            @Override
            protected void onClientConnected(Socket clientSocket) {
                connected[0] = true;

                synchronized (connectedLock) {
                    connectedLock.notifyAll();
                }
            }
        };

        echoServer.start();

        TcpClientSocket clientSocket = new TcpClientSocket();
        clientSocket.setListener(new ITcpClientSocketListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onConnectFailed() {

            }

            @Override
            public void onDisconnected() {

            }

            @Override
            public void onReceive(int length) {

            }

            @Override
            public void onSend(int length) {

            }
        });

        clientSocket.connect("localhost", ECHO_SERVER_PORT);

        synchronized (connectedLock) {
            connectedLock.wait(5000);
        }

        assertTrue(connected[0]);

        echoServer.stop();
    }

    @Test
    void testReceiveSend() throws IOException, InterruptedException {
        String dataToSend = "Test\0Receive";
        EchoServer echoServer = new EchoServer();

        echoServer.start();

        TcpClientSocket clientSocket = new TcpClientSocket();
        ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);

        Object endEvent = new Object();
        final boolean[] success = {false};

        clientSocket.setListener(new ITcpClientSocketListener() {
            @Override
            public void onConnected() {
                clientSocket.read(receiveBuffer);
                clientSocket.write(ByteBuffer.wrap(dataToSend.getBytes()));
            }

            @Override
            public void onConnectFailed() {

            }

            @Override
            public void onDisconnected() {

            }

            @Override
            public void onReceive(int length) {
                if (new String(receiveBuffer.array(), 0, length).equals(dataToSend)) {
                    success[0] = true;
                }

                synchronized (endEvent) {
                    endEvent.notify();
                }
            }

            @Override
            public void onSend(int length) {

            }
        });

        clientSocket.connect("localhost", ECHO_SERVER_PORT);

        synchronized (endEvent) {
            endEvent.wait(5000);
        }

        assertTrue(success[0]);

        echoServer.stop();
    }

    // Echo tcp server
    public class EchoServer {
        private final ServerSocket serverSocket;

        public EchoServer() throws IOException {
            this.serverSocket = new ServerSocket(ECHO_SERVER_PORT);
        }

        public void start() {
            try {
                new Thread(() -> {
                    try {
                        Socket clientSocket = serverSocket.accept();

                        if (clientSocket != null) {
                            onClientConnected(clientSocket);

                            byte[] buffer = new byte[1024];
                            int bytesRead = clientSocket.getInputStream().read(buffer);
                            onDataReceived(buffer, 0, bytesRead);
                            clientSocket.getOutputStream().write(buffer, 0, bytesRead);
                            clientSocket.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected void onDataReceived(byte[] data, int offset, int length) {
        }

        protected void onClientConnected(Socket clientSocket) {
        }

        public void stop() {
            try {
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
