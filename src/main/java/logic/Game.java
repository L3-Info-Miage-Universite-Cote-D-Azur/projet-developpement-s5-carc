package logic;

import java.util.ArrayList;

import input.PlayerInput;
import logger.Logger;
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

        stack.fill(config);
        started = true;
    }

    public void update() {
        if (!started) {
            throw new IllegalStateException("Game should be started at this point.");
        }

        Logger.info(String.format("[GAME] Turn %d", turn.getCount() + 1));

        turn.startNext();
        turn.getPlayer().onTurn();

        if (isFinished()) {
            onEnd();
        }
    }

    public void onEnd() {
        Logger.info("[GAME] Game over");
    }

    public boolean isFinished(){
        boolean check = false;

        for (Player player : players) {
            if (player.getScore() >= 279 || this.stack.getNumTiles() == 0 ) {
                check = true;
            }
        }

        return check;
    }

    public void createPlayer(PlayerInfo info, PlayerInput input) {
        if (getPlayerCount() >= config.MAX_PLAYERS) {
            throw new TooManyPlayerException();
        }

        players.add(new Player(info, input, this));
    }

    public GameConfig getConfig() {
        return config;
    }

    public GameBoard getBoard() {
        return board;
    }

    public TileStack getStack() {
        return stack;
    }

    public Player getPlayer(int player){
        return players.get(player);
    }

    public int getPlayerCount() {
        return players.size();
    }
}
