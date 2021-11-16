package utils;

import logic.Game;
import logic.board.GameBoard;
import logic.math.Vector2;
import logic.meeple.Meeple;
import logic.player.Player;
import logic.tile.Chunk;
import logic.tile.ChunkId;
import logic.tile.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class for drawing the game.
 */
public class GameDrawUtils {
    private static final int tileWidth = 160;
    private static final int tileHeight = 160;
    private static final int meepleWidth = 40;
    private static final int meepleHeight = 40;

    private static ImageDatabase tileDatabase;
    private static ImageDatabase meepleDatabase;

    /**
     * Loads the images for the rendering and stores them in the image database.
     */
    private static void loadImageDatabaseIfNeeded() {
        if (tileDatabase == null) {
            tileDatabase = new ImageDatabase(tileWidth, tileHeight);

            for (File file : new File("models/tiles").listFiles()) {
                if (file.isFile()) {
                    try {
                        tileDatabase.cache(file.getName().replace(".jpg", ""), ImageIO.read(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (meepleDatabase == null) {
            meepleDatabase = new ImageDatabase(meepleWidth, meepleHeight);

            for (File file : new File("models/meeples").listFiles()) {
                if (file.isFile()) {
                    try {
                        meepleDatabase.cache(file.getName().replace(".png", ""), ImageIO.read(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Calculates the board bounds.
     * @param board The board to calculate the bounds for.
     * @return The bounds of the board.
     */
    public static Bounds calculateBoardBounds(GameBoard board) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = 0;
        int maxY = 0;

        for (Tile tile : board.getTiles()) {
            int x = tile.getPosition().getX();
            int y = tile.getPosition().getY();

            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }

        return new Bounds(minX, minY, maxX, maxY);
    }

    /**
     * Gets the position where the tile should be drawn.
     * @param tile The tile to get the position for.
     * @return The position where the tile should be drawn.
     */
    private static Vector2 getTilePosition(Tile tile) {
        return new Vector2(tile.getPosition().getX() * tileWidth, tile.getPosition().getY() * tileHeight);
    }

    /**
     * Gets the position where the meeple should be drawn.
     * @param tile The tile to get the position for.
     * @param chunkId The chunk id to get the position for.
     * @return The position where the meeple should be drawn.
     */
    private static Vector2 getMeeplePosition(Tile tile, ChunkId chunkId) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the sprite model to use for the specified player.
     * @param player The player to get the sprite model for.
     * @return The sprite model to use for the specified player's meeple.
     */
    private static String getOwnMeepleSpriteModel(Player player) {
        return Integer.toString(player.getId() - 1);
    }

    /**
     * Creates a new image layer representing the specified game instance.
     * @param game The game instance to create the image layer for.
     * @return The image layer representing the specified game instance.
     */
    public static BufferedImage createLayer(Game game) {
        return createLayer(game, calculateBoardBounds(game.getBoard()));
    }

    /**
     * Creates a new image layer representing the specified game instance.
     * @param game The game instance to create the image layer for.
     * @param boardBounds The bounds of the board.
     * @return The image layer representing the specified game instance.
     */
    public static BufferedImage createLayer(Game game, Bounds boardBounds) {
        long startTime = System.currentTimeMillis();

        Bounds layerBounds = boardBounds.scale(tileWidth, tileHeight).reverseY();
        BufferedImage layer = new BufferedImage(layerBounds.getWidth(), layerBounds.getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D layerGraphics = layer.createGraphics();
        render(game, layerGraphics, layerBounds);
        layerGraphics.dispose();

        long endTime = System.currentTimeMillis();

        System.out.println("Time to create image: " + (endTime - startTime) + "ms");

        return layer;
    }

    /**
     * Renders the specified game instance to the specified graphics context.
     * @param game The game instance to render.
     * @param graphics The graphics context to render to.
     * @param layerBounds The bounds of the layer to render.
     */
    public static void render(Game game, Graphics graphics, Bounds layerBounds) {
        loadImageDatabaseIfNeeded();

        graphics.setFont(new Font("Courier New", Font.CENTER_BASELINE | Font.BOLD, 25));
        graphics.setColor(Color.blue);

        for (Tile tile : game.getBoard().getTiles()) {
            Vector2 tileImagePosition = getTilePosition(tile).reverseY().subtract(layerBounds.start);
            BufferedImage tileImage = tileDatabase.get(tile.getConfig().model);

            assert tileImagePosition.getX() >= 0 && tileImagePosition.getY() >= 0;
            assert tileImagePosition.getX() + tileWidth <= layerBounds.getWidth() && tileImagePosition.getY() + tileHeight <= layerBounds.getHeight();

            graphics.drawImage(tileImage, tileImagePosition.getX(), tileImagePosition.getY(), null);
            graphics.drawString(tile.getConfig().model + " " + tile.getPosition().getX() + " " + tile.getPosition().getY(), tileImagePosition.getX() + tileWidth / 4, tileImagePosition.getY() + tileHeight / 2);

            for (ChunkId chunkId : ChunkId.values()) {
                Chunk chunk = tile.getChunk(chunkId);

                if (chunk.hasMeeple()) {
                    Meeple meeple = chunk.getMeeple();
                    BufferedImage meepleImage = meepleDatabase.get(getOwnMeepleSpriteModel(meeple.getOwner()));

                    graphics.drawImage(meepleImage, tileImagePosition.getX(), tileImagePosition.getY(), null);
                }
            }
        }
    }
}
