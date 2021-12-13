package client.service;

import client.Client;
import client.config.StatsConfig;
import client.logger.Logger;
import client.logger.LoggerCategory;
import client.message.IMessageHandler;
import client.stats.GameStatistics;
import client.view.GameBoardView;
import logic.Game;
import network.message.IMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 * Game statistics service that makes statistics about the match.
 */
public class GameStatisticsService extends ServiceBase implements IMessageHandler {
    private static final String SAVE_DIRECTORY = "stats/client_%d";
    private static final String DETAILS_FILENAME = "game_details_%s.txt";
    private static final String VIEW_FILENAME = "game_view_%s.jpg";

    private final StatsConfig config;
    private final ExecutorService backgroundExecutor;
    private final GameStatistics globalStatistics;
    private int statisticsCounter;
    private int userId;

    public GameStatisticsService(Client client) {
        super(client);
        config = client.getConfig().getStatsConfig();
        backgroundExecutor = Executors.newSingleThreadExecutor();
        globalStatistics = new GameStatistics();
    }

    /**
     * Handles the specified message if the handler is interested in it.
     *
     * @param message The message to handle.
     */
    @Override
    public void handleMessage(IMessage message) {
        switch (message.getType()) {
            case SERVER_HELLO -> onAuthenticated();
            default -> { /* do nothing */ }
        }
    }

    /**
     * Called when the client is connected and ready to play.
     */
    private void onAuthenticated() {
        userId = client.getAuthenticationService().getUserId();

        Path saveDirectory = Paths.get(getSaveDirectory());

        try {
            Files.createDirectories(saveDirectory);

            try (Stream<Path> files = Files.list(saveDirectory)) {
                for (File file : files.map(Path::toFile).toList()) {
                    if (file.isFile()) {
                        Files.delete(file.toPath());
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot initialize save directory.");
        }
    }

    /**
     * Called by {@link BattleService} when the battle is over.
     *
     * @param game The game that was played.
     */
    public void onBattleOver(Game game) {
        final int id = ++statisticsCounter;
        GameStatistics statistics = new GameStatistics(game);
        Logger.info(LoggerCategory.SERVICE, "Winner is Player %d".formatted(statistics.getWinner().getId()));
        saveStatisticsInBackground(statistics, Integer.toString(id));

        if (config.isCreateBoardView()) {
            GameBoardView view = new GameBoardView(game);
            saveBoardViewInBackground(view, Integer.toString(id));
        }

        if (config.isCreateGlobalStatistics()) {
            globalStatistics.append(game);
        }
    }

    /**
     * Called when the client is connected to the server.
     * Clears the statistics directory.
     */
    @Override
    public void onConnect() {
        // Nothing to do here.
    }

    /**
     * Called when the client is disconnected from the server.
     */
    @Override
    public void onDisconnect() {
        // Check if we have already done matches.
        if (userId != 0 && config.isCreateGlobalStatistics()) {
            saveStatisticsInBackground(globalStatistics, "global");
        }

        backgroundExecutor.shutdown();
    }

    /**
     * Stores the statistics in the statistics directory using a thread pool.
     *
     * @param statistics The statistics of the game.
     * @param name       The name of the match.
     */
    private void saveStatisticsInBackground(GameStatistics statistics, String name) {
        backgroundExecutor.submit(() -> saveStatistics(statistics, name));
    }

    /**
     * Stores the statistics in the statistics directory.
     *
     * @param statistics The board of the game.
     * @param name       The name of the match.
     */
    private void saveStatistics(GameStatistics statistics, String name) {
        statistics.save(Paths.get(getSaveDirectory(), DETAILS_FILENAME.formatted(name)).toFile());
    }

    /**
     * Stores the board view in the statistics directory using a thread pool.
     *
     * @param view The board of the game.
     * @param name The name of the match.
     */
    private void saveBoardViewInBackground(GameBoardView view, String name) {
        backgroundExecutor.submit(() -> saveBoardView(view, name));
    }

    /**
     * Stores the board view in the statistics directory.
     *
     * @param view The board of the game.
     * @param name The name of the match.
     */
    private void saveBoardView(GameBoardView view, String name) {
        view.save(Paths.get(getSaveDirectory(), VIEW_FILENAME.formatted(name)).toFile());
    }

    /**
     * Gets the save directory.
     *
     * @return The save directory.
     */
    private String getSaveDirectory() {
        return SAVE_DIRECTORY.formatted(userId);
    }
}
