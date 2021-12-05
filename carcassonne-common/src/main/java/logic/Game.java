package logic;

import logic.board.GameBoard;
import logic.command.CommandExecutor;
import logic.config.GameConfig;
import logic.exception.NotEnoughPlayerException;
import logic.exception.TooManyPlayerException;
import logic.player.Player;
import logic.state.GameStartState;
import logic.state.GameState;
import logic.state.GameStateFactory;
import logic.state.GameStateType;
import logic.tile.Tile;
import logic.tile.TileStack;
import logic.tile.chunk.ChunkId;
import stream.ByteInputStream;
import stream.ByteOutputStream;

import java.util.ArrayList;
import java.util.List;

/**
 * The Game class is the main class of the game. It contains all the logic of the game.
 */
public class Game {
    private final GameConfig config;
    private final GameBoard board;
    private final TileStack stack;
    private final ArrayList<Player> players;
    private final CommandExecutor commandExecutor;

    private GameState state;
    private IGameListener listener;

    private boolean master;
    private int turnCount;

    public Game(GameConfig config) {
        this.config = config;
        this.board = new GameBoard();
        this.stack = new TileStack(this);
        this.players = new ArrayList<>(config.maxPlayers);
        this.commandExecutor = new CommandExecutor(this);
        this.master = true;
        this.listener = new IGameListener() {
            @Override
            public void onGameStarted() {

            }

            @Override
            public void onGameOver() {

            }

            @Override
            public void onTurnStarted(int id, Tile tileDrawn) {

            }

            @Override
            public void onTurnEnded(int id) {

            }

            @Override
            public void onStateChanged(GameState state) {

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
        };
    }

    public void start() {
        if (getPlayerCount() < config.minPlayers) {
            throw new NotEnoughPlayerException(getPlayerCount(), config.minPlayers);
        }

        if (!master) {
            throw new IllegalStateException("Game is not master. Only a master game instance need to be started.");
        }

        setState(new GameStartState(this));
    }

    /**
     * Returns if the game is ended.
     *
     * @return
     */
    public boolean isOver() {
        return state != null && state.getType() == GameStateType.OVER;
    }

    /**
     * Returns if the game is started.
     *
     * @return if the game is started
     */
    public boolean isStarted() {
        return state != null;
    }

    /**
     * Adds the given player to the game.
     *
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        if (getPlayerCount() >= config.maxPlayers) {
            throw new TooManyPlayerException();
        }

        players.add(player);
        player.setGame(this);
    }

    /**
     * Gets the configuration of the game.
     *
     * @return the configuration of the game
     */
    public GameConfig getConfig() {
        return config;
    }

    /**
     * Gets the game board.
     *
     * @return the game board
     */
    public GameBoard getBoard() {
        return board;
    }

    /**
     * Gets the tile stack.
     *
     * @return the tile stack
     */
    public TileStack getStack() {
        return stack;
    }

    /**
     * Gets the current state of the game.
     *
     * @return The current state of the game.
     */
    public GameState getState() {
        return state;
    }

    /**
     * Sets and initializes the state of the game.
     *
     * @param state The state to set.
     */
    public void setState(GameState state) {
        this.state = state;
        state.init();

        // As a state can be a transition-state, we need to check
        // if the state passed by parameter is still the current state.
        if (this.state == state) {
            listener.onStateChanged(state);
        }
    }

    /**
     * Gets the list of players.
     *
     * @return the list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the player at the given index.
     *
     * @param index The index of the player.
     * @return Player The player at the given index.
     */
    public Player getPlayer(int index) {
        return players.get(index);
    }

    /**
     * Gets the index of the player in the game logic.
     *
     * @param player The player to get the index of.
     * @return The index of the player.
     */
    public int getPlayerIndex(Player player) {
        return players.indexOf(player);
    }

    /**
     * Gets the player's instance of the given player id.
     *
     * @param id The player id.
     * @return The player instance.
     */
    public Player getPlayerById(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

    /**
     * Gets the number of players in the game.
     *
     * @return The number of players in the game.
     */
    public int getPlayerCount() {
        return players.size();
    }

    /**
     * Returns the listener of this game.
     *
     * @return the listener of this game.
     */
    public IGameListener getListener() {
        return listener;
    }

    /**
     * Sets the listener for this game.
     *
     * @param listener The listener to set.
     */
    public void setListener(IGameListener listener) {
        this.listener = listener;
    }

    /**
     * Gets the command executor.
     *
     * @return the command executor
     */
    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    /**
     * Determines if the game is the master version (aka server / offline game).
     *
     * @return
     */
    public boolean isMaster() {
        return master;
    }

    /**
     * Sets the game as the master version (aka server / offline game).
     *
     * @param master
     */
    public void setMaster(boolean master) {
        this.master = master;
    }

    /**
     * Gets the turn counter.
     *
     * @return turn count
     */
    public int getTurnCount() {
        return turnCount;
    }

    /**
     * Increases the turn counter.
     */
    public void increaseTurnCount() {
        turnCount++;
    }

    /**
     * Gets the executor of the current turn.
     *
     * @return The executor of the current turn.
     */
    public Player getTurnExecutor() {
        if (!isStarted()) {
            throw new IllegalStateException("Game is not started.");
        }

        if (isOver()) {
            throw new IllegalStateException("Game is over.");
        }

        return players.get((turnCount - 1) % players.size());
    }

    /**
     * Encodes the game attributes to the given output stream.
     *
     * @param stream the output stream
     * @param master if the game data to encode is for a master version (aka server / offline game)
     */
    public void encode(ByteOutputStream stream, boolean master) {
        stream.writeInt(turnCount);
        stream.writeInt(players.size());

        for (Player player : players) {
            player.encode(stream);
        }

        if (state != null) {
            stream.writeBoolean(true);

            board.encode(stream, this);

            if (master) {
                stack.encode(stream, this);
            }

            stream.writeInt(state.getType().ordinal());
            state.encode(stream);
        } else {
            stream.writeBoolean(false);
        }
    }

    /**
     * Decodes the game attributes from the given input stream.
     *
     * @param stream the input stream
     * @param master if the game data to decode is for a master version (aka server / offline game)
     */
    public void decode(ByteInputStream stream, boolean master) {
        turnCount = stream.readInt();
        players.clear();

        int playerCount = stream.readInt();

        for (int i = 0; i < playerCount; i++) {
            Player player = new Player();
            player.decode(stream);
            addPlayer(player);
        }

        if (stream.readBoolean()) {
            board.decode(stream, this);

            if (master) {
                stack.decode(stream, this);
                this.master = true;
            } else {
                stack.clear();
                this.master = false;
            }

            state = GameStateFactory.createByType(GameStateType.values()[stream.readInt()], this);
            state.decode(stream);
        } else {
            state = null;
        }
    }

    /**
     * Clones the current game.
     *
     * @return the cloned game
     */
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
