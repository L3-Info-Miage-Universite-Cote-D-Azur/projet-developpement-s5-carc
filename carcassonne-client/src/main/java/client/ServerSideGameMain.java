package client;

import client.config.ServerConfig;
import client.network.ServerConnection;
import logic.Game;
import network.message.matchmaking.JoinMatchmakingMessage;

import java.io.IOException;
import java.util.ArrayList;

public class ServerSideGameMain {
    private static ArrayList<Client> clients = new ArrayList<>();
    private static ArrayList<MatchResult> matchHistory = new ArrayList<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 0; i < ServerConfig.NUM_CLIENTS; i++) {
            clients.add(new Client(ServerConfig.SERVER_IP, ServerConfig.SERVER_PORT));
            Thread.sleep(500);
        }

        Thread.sleep(Long.MAX_VALUE);
    }

    public static void onMatchOver(long ownUserId, Game game) {
        if (clients.get(0).getServerConnection().getMessageHandler().getUserId() == ownUserId) {
            matchHistory.add(new MatchResult(game));
        }

        if (!clients.stream().anyMatch(c -> c.getServerConnection().getMessageHandler().isInMatch())) {
            if (matchHistory.size() == ServerConfig.NUM_MATCHES) {
                onAllMatchOver();
            } else {
                for (Client c : clients) {
                    c.getServerConnection().send(new JoinMatchmakingMessage());
                }
            }
        }
    }

    private static void onAllMatchOver() {
        for (Client client : clients) {
            client.close();
        }
    }

    private static class Client {
        private ServerConnection serverConnection;

        public Client(String host, int port) throws IOException {
            this.serverConnection = new ServerConnection();
            this.serverConnection.connect(host, port);
        }

        public void close() {
            this.serverConnection.close();
        }

        public ServerConnection getServerConnection() {
            return this.serverConnection;
        }
    }

    private static class MatchResult {
        public MatchResult(Game game) {

        }
    }
}
