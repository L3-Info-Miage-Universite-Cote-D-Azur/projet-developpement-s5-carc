package logic.config;

public class GameConfig {
    public class TileConfig {
        private TileType TYPE;
        private int DECK_COUNT;
    }
    private int MAX_PLAYERS;
    private int MIN_PLAYERS;
    private int PLAYER_DECK_CAPACITY;
    Set<TileType, TileConfig> TILES;

    public boolean validate(){

    }
}
