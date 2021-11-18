package client;

import client.config.LoggerConfig;
import client.config.ServerConfig;
import client.logger.Logger;
import client.network.ServerConnection;
import client.utils.GameDrawUtils;
import excel.ExcelNode;
import excel.ExcelRow;
import logic.Game;
import logic.player.Player;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlaveGameMain {
    private static final int NUM_CLIENTS = 2;
    private static final String MATCH_HISTORY_SAVE_DIRECTORY = "match_history";
    private static final String MATCH_HISTORY_RESULT_EXCEL_FILE_NAME = "match_history_result.txt";
    private static final String MATCH_HISTORY_RESULT_VIEW_FILE_NAME = "match_history_result_view.png";

    private static ArrayList<Client> clients = new ArrayList<>();
    private static Object locker = new Object();

    public static void main(String[] args) throws IOException, InterruptedException {
        Logger.setConfig(LoggerConfig.loadFromResources());

        for (int i = 0; i < NUM_CLIENTS; i++) {
            clients.add(new Client(ServerConfig.SERVER_IP, ServerConfig.SERVER_PORT));
        }

        synchronized (locker) {
            locker.wait();
        }

        saveMatchHistory(clients.get(0).getServerConnection().getMessageHandler().getMatchHistory());
    }

    public static void saveMatchHistory(List<Game> matchHistory) {
        Logger.info("Save match history...");
        File parentDirectory = new File(MATCH_HISTORY_SAVE_DIRECTORY);

        if (!parentDirectory.exists()) {
            parentDirectory.mkdir();
        }

        var ref = new Object() {
            int id;
        };
        matchHistory.parallelStream().forEach(game -> {
            int matchId;

            synchronized (ref) {
                matchId = ++ref.id;
            }

            File matchDirectory = new File(MATCH_HISTORY_SAVE_DIRECTORY, "match_" + matchId);

            if (!matchDirectory.exists()) {
                matchDirectory.mkdir();
            }

            try {
                saveMatchHistory(game, matchDirectory.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void saveMatchHistory(Game game, Path path) throws IOException {
        ExcelNode excelNode = new ExcelNode();

        excelNode.addColumn("Name");

        ArrayList<Player> playersSort = new ArrayList<>(game.getPlayerCount());

        for (int i = 0; i < game.getPlayerCount(); i++) {
            playersSort.add(game.getPlayer(i));
        }

        playersSort.sort(Player::compareTo);

        for (int i = 0; i < playersSort.size(); i++) {
            excelNode.addColumn("Player ID " + game.getPlayer(i).getId());
        }

        ExcelRow positionRow = excelNode.createRow("POSITION");
        ExcelRow resultScoreRow = excelNode.createRow("RESULTS (score)");
        ExcelRow roadPointRow = excelNode.createRow("ROAD POINTS");
        ExcelRow townPointRow = excelNode.createRow("TOWN POINTS");
        ExcelRow abbeyPointRow = excelNode.createRow("ABBEY POINTS");
        ExcelRow fieldPointRow = excelNode.createRow("FIELD POINTS");
        ExcelRow partisansPlayedRow = excelNode.createRow("PARTISANS PLAYED");
        ExcelRow partisansRemainedRow = excelNode.createRow("PARTISANS REMAINED");

        for (int i = 0; i < playersSort.size(); i++) {
            Player player = playersSort.get(i);
            String columnName = "Player ID " + player.getId();

            positionRow.add(columnName, Integer.toString(i + 1));
            resultScoreRow.add(columnName, Integer.toString(player.getScore()));
            roadPointRow.add(columnName, Integer.toString(player.getRoadPoints()));
            townPointRow.add(columnName, Integer.toString(player.getTownPoints()));
            abbeyPointRow.add(columnName, Integer.toString(player.getAbbeyPoints()));
            fieldPointRow.add(columnName, Integer.toString(player.getFieldPoints()));
            partisansPlayedRow.add(columnName, Integer.toString(player.getPartisansPlayed()));
            partisansRemainedRow.add(columnName, Integer.toString(player.getPartisansRemained()));
        }

        excelNode.saveToFile(Paths.get(path.toString(), MATCH_HISTORY_RESULT_EXCEL_FILE_NAME).toFile());
        ImageIO.write(GameDrawUtils.createLayer(game), "png", Paths.get(path.toString(), MATCH_HISTORY_RESULT_VIEW_FILE_NAME).toFile());
    }

    public static void onAllMatchOver() {
        synchronized (locker) {
            locker.notify();
        }
    }

    private static class Client {
        private ServerConnection serverConnection;

        public Client(String host, int port) throws IOException {
            this.serverConnection = new ServerConnection();
            this.serverConnection.connect(host, port);
        }

        public ServerConnection getServerConnection() {
            return serverConnection;
        }
    }
}
