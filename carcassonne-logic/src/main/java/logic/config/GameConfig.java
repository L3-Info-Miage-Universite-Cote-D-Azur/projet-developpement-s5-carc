package logic.config;

import excel.ExcelDocument;
import logic.config.excel.TileExcelConfig;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GameConfig {
    public ArrayList<TileExcelConfig> tiles;
    public int minPlayers;
    public int maxPlayers;
    public int startingMeepleCount;

    public GameConfig(ArrayList<TileExcelConfig> tiles, int minPlayers, int maxPlayers, int startingMeepleCount) {
        this.tiles = tiles;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.startingMeepleCount = startingMeepleCount;
    }

    public boolean validate() {
        if (minPlayers < 1) {
            return false;
        }

        if (minPlayers > maxPlayers) {
            return false;
        }

        if (startingMeepleCount < 1) {
            return false;
        }

        return true;
    }

    public static GameConfig loadFromDirectory(String path) {
        ArrayList<TileExcelConfig> tiles = loadTilesFromDirectory(Paths.get(path, "tiles").toString());
        ExcelDocument gameConfigDocument = new ExcelDocument(Paths.get(path, "game.txt").toFile());

        return new GameConfig(tiles,
                Integer.parseInt(gameConfigDocument.getCell("minPlayers", "value")),
                Integer.parseInt(gameConfigDocument.getCell("maxPlayers", "value")),
                Integer.parseInt(gameConfigDocument.getCell("startingMeepleCount", "value")));
    }

    private static ArrayList<TileExcelConfig> loadTilesFromDirectory(String path) {
        File root = new File(path);

        if (!root.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory");
        }

        ArrayList<TileExcelConfig> tiles = new ArrayList<>();

        for (File file : root.listFiles()) {
            if (!file.isDirectory()) {
                tiles.add(new TileExcelConfig(new ExcelDocument(file)));
            }
        }

        return tiles;
    }
}
