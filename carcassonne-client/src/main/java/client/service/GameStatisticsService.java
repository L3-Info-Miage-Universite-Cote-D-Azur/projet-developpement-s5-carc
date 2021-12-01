package client.service;

import client.Client;
import client.logger.Logger;
import client.logger.LoggerCategory;
import client.stats.GameStatistics;
import logic.Game;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Game statistics service that makes statistics about the match.
 */
public class GameStatisticsService extends ServiceBase {
    private static final String SAVE_DIRECTORY = "stats";
    private static final String DETAILS_FILENAME = "game_details_%d.txt";
    private static final String VIEW_FILENAME = "game_view_%d_client_%d.jpg";

    private final ArrayList<GameStatistics> statistics;

    public GameStatisticsService(Client client) {
        super(client);
        this.statistics = new ArrayList<>();
    }

    /**
     * Called by {@link BattleService} when the battle is over.
     *
     * @param game The game that was played.
     */
    public void onBattleOver(Game game) {
        statistics.add(new GameStatistics(game));
    }

    /**
     * Called when the client is connected to the server.
     * Clears the statistics directory.
     */
    @Override
    public void onConnect() {
        File saveDirectory = new File(SAVE_DIRECTORY);

        if (!saveDirectory.exists()) {
            saveDirectory.mkdir();
        } else {
            for (File file : saveDirectory.listFiles()) {
                file.delete();
            }
        }
    }

    /**
     * Called when the client is disconnected from the server.
     * Stores the statistics in the statistics directory.
     */
    @Override
    public void onDisconnect() {
        Logger.info(LoggerCategory.SERVICE, "Saving statistics...");

        for (int i = 0; i < statistics.size(); i++) {
            statistics.get(i).save(Paths.get(SAVE_DIRECTORY, String.format(DETAILS_FILENAME, i)).toFile(), Paths.get(SAVE_DIRECTORY, String.format(VIEW_FILENAME, i,
                    client.getAuthenticationService().getUserId())).toFile());
        }
    }
}
