package client;

import client.config.ClientConfig;
import logic.config.GameConfig;

import java.io.IOException;

/**
 * Main class of the client.
 * Creates, starts and waits for the game to end.
 */
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client(ClientConfig.loadFromResources(), GameConfig.loadFromResources());
        client.start();

        synchronized (client) {
            client.wait();
        }
    }
}
