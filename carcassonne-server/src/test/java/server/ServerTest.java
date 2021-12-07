package server;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServerTest {
    private static final String SERVER_HOST = "localhost";

    @Test
    void testConstructorAndSingleton() throws Exception {
        assertEquals(null, Server.getInstance());

        Server server = new Server(SERVER_HOST, new Random().nextInt(10000) + 40000);

        assertEquals(server, Server.getInstance());
        assertThrows(Exception.class, () -> new Server(SERVER_HOST, new Random().nextInt(10000) + 40000));

        server.destroy();
    }

    @Test
    void testStartStopConnectionClosing() throws Exception {
        int serverPort = new Random().nextInt(10000) + 40000;
        Server server = new Server(SERVER_HOST, serverPort);

        server.start();

        SimpleTcpClient client = new SimpleTcpClient(SERVER_HOST, serverPort);
        Thread.sleep(200);

        assertEquals(1, server.getConnectionManager().count());
        server.stop();
        assertEquals(0, server.getConnectionManager().count());

        server.destroy();
    }
}
