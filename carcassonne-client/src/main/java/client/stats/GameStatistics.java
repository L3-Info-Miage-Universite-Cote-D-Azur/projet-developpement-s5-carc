package client.stats;

import client.logger.Logger;
import client.logger.LoggerCategory;
import excel.ExcelNode;
import excel.ExcelRow;
import logic.Game;
import logic.player.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents the statistics of the game.
 */
public class GameStatistics {
    private final ArrayList<GameStatisticsPlayer> players;

    public GameStatistics() {
        this.players = new ArrayList<>();
    }

    public GameStatistics(Game game) {
        this.players = new ArrayList<>();
        append(game);
    }

    /**
     * Appends the stat
     * @param game the game
     */
    public void append(Game game) {
        for (int i = 0; i < game.getPlayerCount(); i++) {
            Player player = game.getPlayer(i);
            GameStatisticsPlayer gameStatisticsPlayer = getPlayer(player.getId());

            if (gameStatisticsPlayer == null) {
                gameStatisticsPlayer = new GameStatisticsPlayer(player);
                players.add(gameStatisticsPlayer);
            } else {
                gameStatisticsPlayer.append(player);
            }
        }

        Collections.sort(players, Collections.reverseOrder());
    }

    /**
     * Gets the player by id.
     * @param id the id
     * @return the player.
     */
    public GameStatisticsPlayer getPlayer(int id) {
        for (GameStatisticsPlayer player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

    /**
     * Creates the excel file with the details of statistics.
     *
     * @return the excel file with the details of statistics.
     */
    private ExcelNode createDetailsExcel() {
        ExcelNode excelNode = new ExcelNode();

        excelNode.addColumn("Name");

        for (GameStatisticsPlayer gameStatisticsPlayer : players) {
            excelNode.addColumn("Player ID " + gameStatisticsPlayer.getId());
        }

        ExcelRow positionRow = excelNode.createRow("POSITION");
        ExcelRow resultScoreRow = excelNode.createRow("RESULTS (score)");
        ExcelRow roadPointRow = excelNode.createRow("ROAD POINTS");
        ExcelRow townPointRow = excelNode.createRow("TOWN POINTS");
        ExcelRow abbeyPointRow = excelNode.createRow("ABBEY POINTS");
        ExcelRow fieldPointRow = excelNode.createRow("FIELD POINTS");
        ExcelRow partisansPlayedRow = excelNode.createRow("PARTISANS PLAYED");
        ExcelRow partisansRemainedRow = excelNode.createRow("PARTISANS REMAINED");

        for (int i = 0; i < players.size(); i++) {
            GameStatisticsPlayer player = players.get(i);
            String columnName = "Player ID " + player.getId();

            positionRow.add(columnName, Integer.toString(i + 1));
            resultScoreRow.add(columnName, Integer.toString(player.getTotalScore()));
            roadPointRow.add(columnName, Integer.toString(player.getRoadScore()));
            townPointRow.add(columnName, Integer.toString(player.getTownScore()));
            abbeyPointRow.add(columnName, Integer.toString(player.getAbbeyScore()));
            fieldPointRow.add(columnName, Integer.toString(player.getFieldScore()));
            partisansPlayedRow.add(columnName, Integer.toString(player.getPlayedMeeples()));
            partisansRemainedRow.add(columnName, Integer.toString(player.getRemainingMeeples()));
        }

        return excelNode;
    }

    /**
     * Saves the excel file with the details of statistics.
     *
     * @param file the excel file with the details of statistics.
     */
    public void save(File file) {
        try {
            createDetailsExcel().saveToFile(file);
        } catch (IOException e) {
            Logger.error(LoggerCategory.SERVICE, "Failed to save the game statistics. %s", e);
        }
    }
}
