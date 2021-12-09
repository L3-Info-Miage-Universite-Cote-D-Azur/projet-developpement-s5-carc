package logic;

import logic.board.GameBoard;
import logic.command.ICommand;
import logic.config.GameConfig;
import logic.dragon.Dragon;
import logic.dragon.Fairy;
import logic.exception.NotEnoughPlayerException;
import logic.exception.TooManyPlayerException;
import logic.meeple.Meeple;
import logic.player.Player;
import logic.state.GameStartState;
import logic.state.GameState;
import logic.state.GameStateFactory;
import logic.state.GameStateType;
import logic.tile.Tile;
import logic.tile.TileStack;
import logic.tile.chunk.Chunk;
import stream.ByteInputStream;
import stream.ByteOutputStream;

import java.util.ArrayList;
import java.util.List;

/**
 * The Game class is the main class of the game. It contains all the logic of the game.
 */
public class Game {
    /**
     * Game configuration of the game.
     */
    private final GameConfig config;

    /**
     * Game board of the game.
     * Contains all the tiles, the dragon and fairy.
     */
    private final GameBoard board;

    /**
     * Tile stack of the game.
     */
    private final TileStack stack;

    /**
     * List of players of the game.
     */
    private final ArrayList<Player> players;

    /**
     * Current state of the game.
     */
    private GameState state;

    /**
     * Listener of the game.
     * Used to notify when an event in the game happens.
     */
    private IGameListener listener;

    /**
     * Counter of the number of turns.
     */
    private int turnCount;

    /**
     * Indicates if this game instance is a master version of the game.
     */
    private boolean master;

    public Game(GameConfig config) {
        this.config = config;
        this.board = new GameBoard(this);
        this.stack = new TileStack(this);
        this.players = new ArrayList<>(config.maxPlayers);
        this.master = true;
        this.listener = new IGameListener() {
            @Override
            public void onTurnStarted(int turn, Tile tileDrawn) {
                // ignored
            }

            @Override
            public void onTurnEnded(int turn) {
                // ignored
            }

            @Override
            public void onGameStarted() {
                // ignored
            }

            @Override
            public void onGameEnded() {
                // ignored
            }

            @Override
            public void onStateChanged(GameState state) {
                // ignored
            }

            @Override
            public void onTilePlaced(Tile tile) {
                // ignored
            }

            @Override
            public void onTileRotated(Tile tile) {
                // ignored
            }

            @Override
            public void onMeeplePlaced(Chunk chunk, Meeple meeple) {
                // ignored
            }

            @Override
            public void onMeepleRemoved(Chunk chunk, Meeple meeple) {
                // ignored
            }

            @Override
            public void onFairySpawned(Fairy fairy) {
                // ignored
            }

            @Override
            public void onFairyDeath(Fairy fairy) {
                // ignored
            }

            @Override
            public void onDragonSpawned(Dragon dragon) {
                // ignored
            }

            @Override
            public void onDragonDeath(Dragon dragon) {
                // ignored
            }

            @Override
            public void onDragonMove(Dragon dragon) {
                // ignored
            }

            @Override
            public void onCommandExecuted(ICommand command) {
                // ignored
            }

            @Override
            public void onCommandFailed(ICommand command, int errorCode) {
                // ignored
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
     * @return if the game is ended
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
        if (this.state == state) { // TODO ALWAYS TRUE
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
     * Determines if the game is the master version (aka server / offline game).
     *
     * @return if the game is the master version
     */
    public boolean isMaster() {
        return master;
    }

    /**
     * Sets the game as the master version (aka server / offline game).
     *
     * @param master if the game is the master version
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
     * Executes the given command to the game.
     *
     * @param command The command to execute.
     * @return true if the command was executed successfully, false otherwise.
     */
    public boolean executeCommand(ICommand command) {
        if (command.getRequiredState() == state.getType()) {
            int errorCode = command.canBeExecuted(this);

            if (errorCode == ICommand.ERROR_SUCCESS) {
                listener.onCommandExecuted(command);
                command.execute(this);
                return true;
            } else {
                listener.onCommandFailed(command, errorCode);
            }
        }
        return false;
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

            board.encode(stream);

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
            board.decode(stream);

            if (master) {
                stack.decode(stream, this);
            } else {
                stack.clear();
            }

            state = GameStateFactory.createByType(GameStateType.values()[stream.readInt()], this);
            state.decode(stream);
        } else {
            state = null;
        }

        this.master = master;

        assert state != null;
        if (state.getType() == GameStateType.START) {
            state.init();
        }
    }

    /**
     * Clones the current game.
     *
     * @return the cloned game
     */
    public Game clone() {
        ByteOutputStream encodeStream = new ByteOutputStream(1000);
        Game game = new Game(config); // TODO Useless
        encode(encodeStream, master);
        Game gameNew = new Game(config);
        ByteInputStream decodeStream = new ByteInputStream(encodeStream.getBytes(), encodeStream.getLength());
        gameNew.decode(decodeStream, master);
        assert decodeStream.getBytesLeft() == 0;
        return gameNew;
    }
}
