package logic;

import logic.board.GameBoard;
import logic.command.CommandExecutor;
import logic.config.GameConfig;
import logic.exception.NotEnoughPlayerException;
import logic.exception.TooManyPlayerException;
import logic.player.Player;
import logic.tile.ChunkId;
import logic.tile.Tile;
import logic.tile.TileStack;
import stream.ByteInputStream;
import stream.ByteOutputStream;

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
    private final CommandExecutor commandExecutor;

    private IGameListener listener;

    private boolean started;
    private boolean ended;
    private boolean master;

    public Game(GameConfig config) {
        this.config = config;
        this.board = new GameBoard();
        this.turn = new GameTurn(this);
        this.stack = new TileStack();
        this.players = new ArrayList<>(config.maxPlayers);
        this.commandExecutor = new CommandExecutor(this);
        this.master = true;
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
        };
    }

    public void start() {
        if (getPlayerCount() < config.minPlayers) {
            throw new NotEnoughPlayerException(getPlayerCount(), config.minPlayers);
        }

        if (!master) {
            throw new IllegalStateException("Game is not master. Only a master game instance need to be started.");
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

        turn.playTurn();
    }

    public void onEnd() {
        listener.onEnd();
        ended = true;
    }

    public boolean isOver() {
        return ended;
    }

    public boolean isStarted() {
        return started;
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

    public int getPlayerIndex(Player player) {
        return players.indexOf(player);
    }

    public Player getPlayerById(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
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

    /**
     * Gets the command executor.
     * @return the command executor
     */
    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    /**
     * Determines if the game is the master version (aka server / offline game).
     * @return
     */
    public boolean isMaster() {
        return master;
    }

    /**
     * Sets the game as the master version (aka server / offline game).
     * @param master
     */
    public void setMaster(boolean master) {
        this.master = master;
    }

    public void encode(ByteOutputStream stream, boolean master) {
        stream.writeBoolean(started);
        stream.writeBoolean(ended);
        stream.writeInt(players.size());

        for (Player player : players) {
            player.encode(stream);
        }

        board.encode(stream, this);

        if (master) {
            stack.encode(stream, this);
        }

        turn.encode(stream, this);
    }

    public void decode(ByteInputStream stream, boolean master) {
        started = stream.readBoolean();
        ended = stream.readBoolean();

        players.clear();

        int playerCount = stream.readInt();

        for (int i = 0; i < playerCount; i++) {
            Player player = new Player();
            player.decode(stream);
            addPlayer(player);
        }

        board.decode(stream, this);

        if (master) {
            stack.decode(stream, this);
            this.master = true;
        } else {
            stack.clear();
            this.master = false;
        }

        turn.decode(stream, this);
    }

    public Game clone() {
        ByteOutputStream encodeStream = new ByteOutputStream(1000);
        Game game = new Game(config);
        encode(encodeStream, master);
        ByteInputStream decodeStream = new ByteInputStream(encodeStream.getBytes(), encodeStream.getLength());
        game.decode(decodeStream, master);
        assert decodeStream.getBytesLeft() == 0;
        return game;
    }
}
