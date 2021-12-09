package client.stats;

import client.logger.Logger;
import client.logger.LoggerCategory;
import client.utils.GameDrawUtils;
import excel.ExcelNode;
import excel.ExcelRow;
import logic.Game;
import logic.player.Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;

/**
 * Represents the statistics of the game.
 */
public class GameStatistics {
    private final ArrayList<GameStatisticsPlayer> players;
    private final BufferedImage boardView;

    public GameStatistics(Game game) {
        this.players = new ArrayList<>();
        this.boardView = GameDrawUtils.createLayer(game);

        ArrayList<Player> sortedPlayersByScore = new ArrayList<>();
        for (int i = 0; i < game.getPlayerCount(); i++)
            sortedPlayersByScore.add(game.getPlayer(i));

        sortedPlayersByScore.sort(Player::compareTo);

        for (int i = 0; i < sortedPlayersByScore.size(); i++) {
            Player player = game.getPlayer(i);
            GameStatisticsPlayer playerStatistics = new GameStatisticsPlayer(player, i + 1);

            this.players.add(playerStatistics);
        }
    }

    /**
     * Creates the excel file with the details statistics.
     *
     * @return the excel file with the details statistics.
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
     * @param detailsFile the excel file with the details of statistics.
     * @param viewFile    the excel file with the board view.
     */
    public void save(File detailsFile, File viewFile) {
        try {
            createDetailsExcel().saveToFile(detailsFile);

            try (FileOutputStream fileOutputStream = new FileOutputStream(viewFile)) {
                FileChannel channel = fileOutputStream.getChannel();
                FileLock lock = channel.lock();

                try {
                    ImageIO.write(boardView, "jpg", fileOutputStream);
                } finally {
                    lock.release();
                }
            }
        } catch (Exception e) {
            Logger.error(LoggerCategory.SERVICE, "Failed to save the game statistics. %s", e);
        }
    }
}
