package logic.config;

import excel.ExcelNode;
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
        ExcelNode gameConfigDocument = ExcelNode.load(Paths.get(path, "game.txt"));

        return new GameConfig(tiles,
                Integer.parseInt(gameConfigDocument.getRow("MinPlayers").getValue("Value")),
                Integer.parseInt(gameConfigDocument.getRow("MaxPlayers").getValue("Value")),
                Integer.parseInt(gameConfigDocument.getRow("StartingMeepleCount").getValue("Value")));
    }

    private static ArrayList<TileExcelConfig> loadTilesFromDirectory(String path) {
        File root = new File(path);

        if (!root.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory");
        }

        ArrayList<TileExcelConfig> tiles = new ArrayList<>();

        for (File file : root.listFiles()) {
            if (!file.isDirectory()) {
                tiles.add(new TileExcelConfig(ExcelNode.load(file.toPath())));
            }
        }

        return tiles;
    }
}
