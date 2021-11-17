package logic;

import logic.board.GameBoard;
import logic.config.GameConfig;
import logic.exception.NotEnoughPlayerException;
import logic.exception.TooManyPlayerException;
import logic.player.Player;
import logic.tile.ChunkId;
import logic.tile.Tile;
import logic.tile.TileStack;

import java.util.ArrayList;

/**
 * The Game class is the main class of the game. It contains all the logic of the game.
 */
public class Game {
    private final GameConfig config;
    private final GameBoard board;
    private final GameTurn turn;
    private final TileStack stack;
    private final ArrayList<Player> players;

    private IGameListener listener;

    private boolean started;
    private boolean ended;

    public Game(GameConfig config) {
        this.config = config;
        this.board = new GameBoard();
        this.turn = new GameTurn(this);
        this.stack = new TileStack();
        this.players = new ArrayList<>(config.maxPlayers);
        this.listener = new IGameListener() {
            @Override
            public void onTurnStarted(int id) {
            }

            @Override
            public void onTurnEnded(int id) {
            }

            @Override
            public void onTilePlaced(Tile tile) {
            }

            @Override
            public void onMeeplePlaced(Player player, Tile tile, ChunkId chunkId) {
            }

            @Override
            public void onMeepleRemoved(Player player, Tile tile, ChunkId chunkId) {
                
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onEnd() {
            }

            @Override
            public void onCommandFailed(String reason) {

            }

            @Override
            public void onCommandFailed(String reason, Object... args) {

            }
        };
    }

    public void start() {
        if (getPlayerCount() < config.minPlayers) {
            throw new NotEnoughPlayerException(getPlayerCount(), config.minPlayers);
        }

        if (started) {
            board.clear();
            turn.reset();
        }

        for (Player player : players) {
            player.init();
        }

        stack.fill(config);
        stack.shuffle();
        started = true;

        listener.onStart();
    }

    public void update() {
        if (!started) {
            throw new IllegalStateException("Game is not started yet.");
        }

        if (ended) {
            throw new IllegalStateException("Game is already finished.");
        }

        if (turn.isOver()) {
            if (!turn.playTurn()) {
                onEnd();
            }
        }
    }

    public void updateToEnd() {
        while (!isOver()) {
            update();
        }
    }

    public void onEnd() {
        listener.onEnd();
        ended = true;
    }

    public boolean isOver() {
        return ended;
    }

    public void addPlayer(Player player) {
        if (getPlayerCount() >= config.maxPlayers) {
            throw new TooManyPlayerException();
        }

        players.add(player);
        player.setGame(this);
    }

    public Player getWinner() {
        if (!isOver()) {
            throw new IllegalStateException("getWinner() must be called if the game is finished.");
        }

        Player winner = null;
        int winnerScore = -1;

        for (Player p : players) {
            if (p.getScore() > winnerScore) {
                winnerScore = p.getScore();
                winner = p;
            }
        }

        return winner;
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

    public GameTurn getTurn() {
        return turn;
    }

    public Player getPlayer(int player) {
        return players.get(player);
    }

    public int getPlayerCount() {
        return players.size();
    }

    public IGameListener getListener() {
        return listener;
    }

    public void setListener(IGameListener listener) {
        this.listener = listener;
    }
}
