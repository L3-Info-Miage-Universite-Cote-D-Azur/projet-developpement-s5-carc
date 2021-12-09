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
    private final List<TileConfig> tiles;
    private final int minPlayers;
    private final int maxPlayers;
    private final int startingMeepleCount;

    public GameConfig(List<TileConfig> tiles, int minPlayers, int maxPlayers, int startingMeepleCount) {
        this.tiles = tiles;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.startingMeepleCount = startingMeepleCount;
    }

    /**
     * Gets the tile configurations.
     * @return
     */
    public List<TileConfig> getTiles() {
        return tiles;
    }

    /**
     * Gets the tile configuration at the given index.
     * @param index the index of the tile
     * @return the tile configuration
     */
    public TileConfig getTile(int index) {
        return tiles.get(index);
    }

    /**
     * Gets the index of the given tile in the configuration list.
     * @param tile the tile to search for
     * @return the index of the tile in the configuration list
     */
    public int getTileIndex(TileConfig tile) {
        return tiles.indexOf(tile);
    }

    /**
     * Gets the number of minimum players.
     * @return the number of minimum players
     */
    public int getMinPlayers() {
        return minPlayers;
    }

    /**
     * Gets the number of maximum players.
     * @return the number of maximum players
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Gets the number of starting meeple count.
     * @return the number of starting meeple count
     */
    public int getStartingMeepleCount() {
        return startingMeepleCount;
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
}
