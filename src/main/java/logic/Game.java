package logic;

import java.util.ArrayList;

import logic.board.GameBoard;
import logic.config.GameConfig;
import logic.exception.TooManyPlayerException;
import logic.player.Player;

public class Game {
    private final GameConfig config;
    private final GameBoard board;
    private final GameTurn turn;
    private final ArrayList<Player> players;

    private boolean started;

    public Game(GameConfig config) {
        this.config = config;
        this.board = new GameBoard();
        this.turn = new GameTurn(this);
        this.players = new ArrayList<>(config.MAX_PLAYERS);
    }

    public void start() {
        if (started) {
            throw new IllegalStateException("Game can be started only once.");
        }

        started = true;
    }

    public void update() {
        if (!started) {
            throw new IllegalStateException("Game should be started at this point.");
        }

        turn.startNext();
        turn.getPlayer().onTurn();

        if (isFinished()) {
            onEnd();
        }
    }

    public void onEnd() {

    }

    public boolean isFinished(){
        return false;
    }

    public void addPlayer(Player player) {
        if (getPlayerCount() >= config.MAX_PLAYERS) {
            throw new TooManyPlayerException();
        }

        players.add(player);
    }

    public GameConfig getGameConfig() {
        return config;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public Player getPlayer(int player){
        return players.get(player);
    }

    public GameBoard getGameBoard(){
        return board;
    }
}
