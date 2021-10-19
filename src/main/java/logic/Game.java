package logic;

import java.util.ArrayList;

import input.PlayerInput;
import logic.board.GameBoard;
import logic.config.GameConfig;
import logic.exception.TooManyPlayerException;
import logic.player.Player;
import logic.player.PlayerInfo;
import logic.tile.TileStack;

public class Game {
    private final GameConfig config;
    private final GameBoard board;
    private final GameTurn turn;
    private final TileStack stack;
    private final ArrayList<Player> players;

    private boolean started;

    public Game(GameConfig config) {
        this.config = config;
        this.board = new GameBoard();
        this.turn = new GameTurn(this);
        this.stack = new TileStack();
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
        // Check if one of player has 279 or more points. If it's true game is Finished
        return this.stack.getNumTiles() == 0;
    }

    public void createPlayer(PlayerInfo info, PlayerInput input) {
        if (getPlayerCount() >= config.MAX_PLAYERS) {
            throw new TooManyPlayerException();
        }

        players.add(new Player(info, input, this));
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

    public GameBoard getGameBoard() {
        return board;
    }

    public TileStack getStack() {
        return stack;
    }
}
