package client.config;

import logic.config.GameConfig;

public class ServerConfig {
    public static final String SERVER_IP = "127.0.0.1";
    public static final int SERVER_PORT = 8080;
    public static final int NUM_MATCHES = 500;
    public static final int MATCHMAKING_MATCH_CAPACITY = 2;
    public static GameConfig GAME_CONFIG = GameConfig.loadFromResources();
}
