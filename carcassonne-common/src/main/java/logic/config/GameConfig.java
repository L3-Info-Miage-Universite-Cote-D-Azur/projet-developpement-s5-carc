package logic.config;

import excel.ExcelNode;
import logic.config.excel.TileExcelConfig;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Represents the game configuration.
 */
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

    /**
     * Validates the game configuration.
     * @return true if the game configuration is valid, false otherwise
     */
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

    /**
     * Loads the game configuration from the given path.
     * @return the game configuration
     */
    public static GameConfig loadFromResources() {
        try {
            Path tilePath = Path.of(GameConfig.class.getResource("tiles").toURI());
            System.out.println("OK 0: "+ new File(tilePath.toString()).exists());
            Path gamePath = Path.of(GameConfig.class.getResource("game.txt").toURI());
            System.out.println("OK 1: "+ new File(gamePath.toString()).exists());

            ArrayList<TileExcelConfig> tiles = loadTilesFromDirectory(tilePath.toString());
            ExcelNode gameConfigDocument = ExcelNode.load(gamePath.toString());

            return new GameConfig(tiles,
                    Integer.parseInt(gameConfigDocument.getRow("MinPlayers").getValue("Value")),
                    Integer.parseInt(gameConfigDocument.getRow("MaxPlayers").getValue("Value")),
                    Integer.parseInt(gameConfigDocument.getRow("StartingMeepleCount").getValue("Value")));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads the tiles from the given path.
     * @param path the path to the tiles
     * @return the tiles configuration
     */
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
