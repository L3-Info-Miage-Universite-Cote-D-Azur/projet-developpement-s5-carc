package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Utils.OutputUtils;
import logger.Logger;
import logic.board.GameBoard;
import logic.config.GameConfig;
import logic.exception.NotEnoughPlayerException;
import logic.exception.TooManyPlayerException;
import logic.player.PlayerBase;
import logic.tile.TileStack;

public class Game {
    private final GameConfig config;
    private final GameBoard board;
    private final GameTurn turn;
    private final TileStack stack;
    private final ArrayList<PlayerBase> players;

    private boolean started;

    public Game(GameConfig config) {
        this.config = config;
        this.board = new GameBoard();
        this.turn = new GameTurn(this);
        this.stack = new TileStack();
        this.players = new ArrayList<>(config.MAX_PLAYERS);
    }

    public void start() {
        if (getPlayerCount() < config.MIN_PLAYERS) {
            throw new NotEnoughPlayerException(getPlayerCount(), config.MIN_PLAYERS);
        }

        if (started) {
            board.clear();
            turn.reset();
        }

        for (PlayerBase player : players) {
            player.init();
        }

        stack.fill(config);
        stack.shuffle();
        started = true;
    }

    public void update() {
        if (!started) {
            throw new IllegalStateException("Game should be started at this point.");
        }

        turn.playTurn();

        if (isFinished()) {
            onEnd();
        }
    }

    public void updateToEnd() {
        while (!isFinished()) {
            update();
        }
    }

    public void onEnd() {
        Logger.info("[GAME] Game over");
        Logger.info("[STATS] Winner is Player %d !", getWinner().getId());
    }

    public boolean isFinished() {
        for (PlayerBase player : players) {
            if (player.getScore() >= 279) {
                return true;
            }
        }

        return this.stack.getNumTiles() == 0;
    }

    public void addPlayer(PlayerBase player) {
        if (getPlayerCount() >= config.MAX_PLAYERS) {
            throw new TooManyPlayerException();
        }

        players.add(player);
        player.setGame(this);
    }

    public PlayerBase getWinner() {
        if (!isFinished()) {
            throw new IllegalStateException("getWinner() must be called if the game is finished.");
        }

        PlayerBase winner = null;
        int winnerScore = -1;

        for (PlayerBase p : players) {
            if (p.getScore() > winnerScore) {
                winnerScore = p.getScore();
                winner = p;
            }
        }

        return winner;
    }

    public String getStat(int cellSize) {
        StringBuilder stat = new StringBuilder("| Statistics |\n");

        // Sort the player list by the score
        ArrayList<PlayerBase> playersSort = new ArrayList<>(players);
        playersSort.sort(PlayerBase::compareTo);
        Collections.reverse(playersSort);

        // Player Name
        List<String> playerName = new ArrayList<>();
        playersSort.forEach(p -> playerName.add("Player " + p.getId()));
        OutputUtils.toArrayLine("PLAYER", cellSize, playerName, stat);

        // Total Score
        List<String> playerResult = new ArrayList<>();
        final int[] i = {1};
        playersSort.forEach(p -> {
            playerResult.add(i[0] + "e(" + p.getScore() + ")");
            i[0]++;
        });
        OutputUtils.toArrayLine("RESULTS (score)", cellSize, playerResult, stat);

        // Road points
        List<String> playerRoadPoints = new ArrayList<>();
        playersSort.forEach(p -> playerRoadPoints.add(String.valueOf(p.getRoadPoints())));
        OutputUtils.toArrayLine("ROAD POINTS", cellSize, playerRoadPoints, stat);

        // Town points
        List<String> playerTownPoints = new ArrayList<>();
        playersSort.forEach(p -> playerTownPoints.add(String.valueOf(p.getTownPoints())));
        OutputUtils.toArrayLine("TOWN POINTS", cellSize, playerTownPoints, stat);

        // Abbey points
        List<String> playerAbbeyPoints = new ArrayList<>();
        playersSort.forEach(p -> playerAbbeyPoints.add(String.valueOf(p.getAbbeyPoints())));
        OutputUtils.toArrayLine("ABBEY POINTS", cellSize, playerAbbeyPoints, stat);

        // Field points
        List<String> playerFieldPoints = new ArrayList<>();
        playersSort.forEach(p -> playerFieldPoints.add(String.valueOf(p.getFieldPoints())));
        OutputUtils.toArrayLine("FIELD POINTS", cellSize, playerFieldPoints, stat);

        // Partisans played
        List<String> playerPartisansPlayed = new ArrayList<>();
        playersSort.forEach(p -> playerPartisansPlayed.add(String.valueOf(p.getPartisansPlayed())));
        OutputUtils.toArrayLine("PARTISANS PLAYED", cellSize, playerPartisansPlayed, stat);

        // Partisans remained
        List<String> playerPartisansRemained = new ArrayList<>();
        playersSort.forEach(p -> playerPartisansRemained.add(String.valueOf(p.getPartisansRemained())));
        OutputUtils.toArrayLine("PARTISANS REMAINED", cellSize, playerPartisansRemained, stat);

        return stat.toString();
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

    public PlayerBase getPlayer(int player) {
        return players.get(player);
    }

    public int getPlayerCount() {
        return players.size();
    }
}
