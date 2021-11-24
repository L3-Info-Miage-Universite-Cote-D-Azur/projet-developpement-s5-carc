package client.stats;

import client.utils.GameDrawUtils;
import excel.ExcelNode;
import excel.ExcelRow;
import logic.Game;
import logic.player.Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * Represents the statistics of the game.
 */
public class GameStatistics {
    private static final Object writeLock = new Object();

    private final ArrayList<GameStatisticsPlayer> players;
    private final BufferedImage boardView;

    public GameStatistics(Game game) {
        this.players = new ArrayList<>();
        this.boardView = GameDrawUtils.createLayer(game);

        ArrayList<Player> sortedPlayersByScore = new ArrayList<>() {{
            for (int i = 0; i < game.getPlayerCount(); i++) {
                add(game.getPlayer(i));
            }

            sort(Player::compareTo);
        }};

        for (int i = 0; i < sortedPlayersByScore.size(); i++) {
            Player player = game.getPlayer(i);
            GameStatisticsPlayer playerStatistics = new GameStatisticsPlayer(player, i + 1);

            this.players.add(playerStatistics);
        }
    }

    /**
     * Creates the excel file with the details statistics.
     * @return the excel file with the details statistics.
     */
    private ExcelNode createDetailsExcel() {
        ExcelNode excelNode = new ExcelNode();

        excelNode.addColumn("Name");

        for (int i = 0; i < players.size(); i++) {
            excelNode.addColumn("Player ID " + players.get(i).getId());
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
     * Saves the excel file with the details statistics.
     * @param detailsFile the excel file with the details statistics.
     * @param viewFile the excel file with the board view.
     */
    public void save(File detailsFile, File viewFile) {
        synchronized (writeLock) {
            try {
                createDetailsExcel().saveToFile(detailsFile);
                ImageIO.write(boardView, "png", viewFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
