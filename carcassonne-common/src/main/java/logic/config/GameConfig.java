package logic.config;

import excel.ExcelNode;
import logic.config.excel.TileConfig;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game configuration.
 */
public class GameConfig {
    // TODO Make static final constant or non-public and provide accessors if needed.
    public List<TileConfig> tiles;
    public int minPlayers;
    public int maxPlayers;
    public int startingMeepleCount;

    public GameConfig(List<TileConfig> tiles, int minPlayers, int maxPlayers, int startingMeepleCount) {
        this.tiles = tiles;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.startingMeepleCount = startingMeepleCount;
    }

    /**
     * Loads the game configuration from the given path.
     *
     * @return the game configuration
     */
    public static GameConfig loadFromResources() {
        try {
            String resourcePath = Path.of(GameConfig.class.getResource(".").toURI()).toString();

            ArrayList<TileConfig> tiles = loadTilesFromDirectory(Paths.get(resourcePath, "tiles").toString());
            ExcelNode gameConfigDocument = ExcelNode.load(Paths.get(resourcePath, "game.txt"));

            String value = "Value";
            return new GameConfig(tiles,
                    Integer.parseInt(gameConfigDocument.getRow("MinPlayers").getValue(value)),
                    Integer.parseInt(gameConfigDocument.getRow("MaxPlayers").getValue(value)),
                    Integer.parseInt(gameConfigDocument.getRow("StartingMeepleCount").getValue(value)));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * Loads the tiles from the given path.
     *
     * @param path the path to the tiles
     * @return the tiles configuration
     */
    private static ArrayList<TileConfig> loadTilesFromDirectory(String path) {
        File root = new File(path);

        if (!root.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory");
        }

        ArrayList<TileConfig> tiles = new ArrayList<>();

        for (File file : root.listFiles()) {
            if (!file.isDirectory()) {
                tiles.add(new TileConfig(ExcelNode.load(file.toPath())));
            }
        }

        return tiles;
    }

    /**
     * Validates the game configuration.
     *
     * @return true if the game configuration is valid, false otherwise
     */
    public boolean validate() {
        if (minPlayers < 1) {
            return false;
        }

        if (minPlayers > maxPlayers) {
            return false;
        }

        return startingMeepleCount >= 1;
    }
}
