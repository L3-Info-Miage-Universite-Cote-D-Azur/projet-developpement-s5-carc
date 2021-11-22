package client;

import client.config.ClientConfig;
import logic.config.GameConfig;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client(ClientConfig.loadFromResources(), GameConfig.loadFromResources());

        synchronized (client) {
            client.wait();
        }
    }
}
