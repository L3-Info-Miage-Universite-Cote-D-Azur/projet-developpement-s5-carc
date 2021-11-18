import network.ServerConnection;

import java.io.IOException;
import java.util.ArrayList;

public class ServerSideGameMain {
    private static String SERVER_IP = "127.0.0.1";
    private static int SERVER_PORT = 8080;

    private static ArrayList<ServerConnection> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 0; i < 5; i++) {
            ServerConnection serverConnection = new ServerConnection();
            serverConnection.connect(SERVER_IP, SERVER_PORT);

            clients.add(serverConnection);

            Thread.sleep(500);
        }

        Thread.sleep(Long.MAX_VALUE);
    }
}
