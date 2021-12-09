package client.view;

import logic.Game;
import logic.board.GameBoard;
import logic.dragon.Dragon;
import logic.math.Vector2;
import logic.meeple.Meeple;
import logic.tile.Tile;
import logic.tile.TileRotation;
import logic.tile.area.Area;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import static client.view.ChunkPositionConstants.*;

/**
 * Class used to build a view of the game board.
 */
public class GameBoardViewBuilder {
    private static final int TILE_WIDTH = 160;
    private static final int TILE_HEIGHT = 160;
    private static final int MEEPLE_WIDTH = 27;
    private static final int MEEPLE_HEIGHT = 27;
    private static final int EXTRA_WIDTH = 40;
    private static final int EXTRA_HEIGHT = 40;
    private static final String EXTRA_PATH = "models/pattern";
    private static final String DRAGONS_PATH = "models/dragons";
    private static final String MEEPLE_PATH = "models/meeples";
    private static final String TILE_PATH = "models/tiles";

    private static final Random rand = new Random();
    private static final EnumMap<ChunkId, Vector2> meepleOffset;
    private static final EnumMap<ChunkId, Polygon> chunksGeo;
    private static ImageDatabase tileDatabase;
    private static ImageDatabase meepleDatabase;
    private static ImageDatabase dragonDatabase;
    private static ImageDatabase extraDatabase;

    static {
        meepleOffset = new EnumMap<>(ChunkId.class);
        meepleOffset.put(ChunkId.NORTH_LEFT, new Vector2(27, 0));
        meepleOffset.put(ChunkId.NORTH_MIDDLE, new Vector2(67, 18));
        meepleOffset.put(ChunkId.NORTH_RIGHT, new Vector2(106, 0));
        meepleOffset.put(ChunkId.SOUTH_LEFT, new Vector2(27, 133));
        meepleOffset.put(ChunkId.SOUTH_MIDDLE, new Vector2(67, 124));
        meepleOffset.put(ChunkId.SOUTH_RIGHT, new Vector2(106, 133));
        meepleOffset.put(ChunkId.WEST_TOP, new Vector2(0, 27));
        meepleOffset.put(ChunkId.WEST_MIDDLE, new Vector2(18, 71));
        meepleOffset.put(ChunkId.WEST_BOTTOM, new Vector2(0, 106));
        meepleOffset.put(ChunkId.EAST_TOP, new Vector2(133, 27));
        meepleOffset.put(ChunkId.EAST_MIDDLE, new Vector2(124, 71));
        meepleOffset.put(ChunkId.EAST_BOTTOM, new Vector2(133, 106));
        meepleOffset.put(ChunkId.CENTER_MIDDLE, new Vector2(67, 67));

        chunksGeo = new EnumMap<>(ChunkId.class);
        chunksGeo.put(ChunkId.NORTH_LEFT, new Polygon(A, B, F)); // ABF
        chunksGeo.put(ChunkId.NORTH_MIDDLE, new Polygon(B, C, G, F)); // BCGF
        chunksGeo.put(ChunkId.NORTH_RIGHT, new Polygon(C, D, G)); // CDG
        chunksGeo.put(ChunkId.SOUTH_LEFT, new Polygon(M, J, N)); // MJN
        chunksGeo.put(ChunkId.SOUTH_MIDDLE, new Polygon(J, K, O, N)); // JKON
        chunksGeo.put(ChunkId.SOUTH_RIGHT, new Polygon(K, P, O)); // KPO
        chunksGeo.put(ChunkId.WEST_TOP, new Polygon(A, E, F)); // AEF
        chunksGeo.put(ChunkId.WEST_MIDDLE, new Polygon(E, F, J, I)); // EFJI
        chunksGeo.put(ChunkId.WEST_BOTTOM, new Polygon(I, J, M)); // IJM
        chunksGeo.put(ChunkId.EAST_TOP, new Polygon(G, H, D)); // GHD
        chunksGeo.put(ChunkId.EAST_MIDDLE, new Polygon(G, H, L, K)); // GHLK
        chunksGeo.put(ChunkId.EAST_BOTTOM, new Polygon(K, L, P)); // KLP
        chunksGeo.put(ChunkId.CENTER_MIDDLE, new Polygon(F, G, K, J)); // FGKJ
    }

    private GameBoardViewBuilder() {
        // ignored
    }

    /**
     * Loads the images for the rendering and stores them in the image database.
     */
    private static void loadImageDatabaseIfNeeded() {
        loadTileDatabase();
        loadMeepleDatabase();
        loadDragonDatabase();
        loadExtraDatabase();
    }

    private static void loadTileDatabase() {
        if (tileDatabase == null) {
            tileDatabase = new ImageDatabase(TILE_WIDTH, TILE_HEIGHT);

            for (File file : Objects.requireNonNull(new File(TILE_PATH).listFiles())) {
                if (file.isFile()) {
                    try {
                        BufferedImage image = ImageIO.read(file);
                        String name = file.getName().replace(".jpg", "");

                        for (int i = 0; i < TileRotation.NUM_ROTATIONS; i++) {
                            tileDatabase.cache(String.format("%s_%d", name, i * TileRotation.ANGLE_ROTATION), rotateImage(image, i * TileRotation.ANGLE_ROTATION));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void loadMeepleDatabase() {
        if (meepleDatabase == null) {
            meepleDatabase = new ImageDatabase(MEEPLE_WIDTH, MEEPLE_HEIGHT);

            for (File file : Objects.requireNonNull(new File(MEEPLE_PATH).listFiles())) {
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

    private static void loadDragonDatabase() {
        if (dragonDatabase == null) {
            dragonDatabase = new ImageDatabase(TILE_WIDTH, TILE_HEIGHT);

            for (File file : Objects.requireNonNull(new File(DRAGONS_PATH).listFiles())) {
                if (file.isFile()) {
                    try {
                        dragonDatabase.cache(file.getName().replace(".png", ""), ImageIO.read(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void loadExtraDatabase() {
        if (extraDatabase == null) {
            extraDatabase = new ImageDatabase(EXTRA_WIDTH, EXTRA_HEIGHT);

            for (File file : Objects.requireNonNull(new File(EXTRA_PATH).listFiles())) {
                if (file.isFile()) {
                    try {
                        extraDatabase.cache(file.getName().replace(".png", ""), ImageIO.read(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static BufferedImage rotateImage(BufferedImage image, int angle) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage newImage = new BufferedImage(width, height, image.getType());
        Graphics2D g = newImage.createGraphics();

        g.translate((width - height) / 2, (height - width) / 2);
        g.rotate(Math.toRadians(angle), height / 2d, width / 2d);
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return newImage;
    }

    /**
     * Calculates the board bounds.
     *
     * @param board The board to calculate the bounds for.
     * @return The bounds of the board.
     */
    public static Bounds calculateBoardBounds(GameBoard board) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = 0;
        int maxY = 0;

        for (Tile tile : board.getTiles()) {
            int x = tile.getPosition().x();
            int y = tile.getPosition().y();

            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }

        return new Bounds(minX - 2, minY - 2, maxX + 2, maxY + 2);
    }

    /**
     * Gets the position where the tile should be drawn.
     *
     * @param vector2 The tile position to get the position for.
     * @return The position where the tile should be drawn.
     */
    private static Vector2 getTilePosition(Vector2 vector2) {
        return new Vector2(vector2.x() * TILE_WIDTH, vector2.y() * TILE_HEIGHT);
    }

    /**
     * Gets the position where the meeple should be drawn.
     *
     * @param tilePosition The tile position to add the meeple position.
     * @param chunkId      The chunk id to get the position for.
     * @return The position where the meeple should be drawn.
     */
    private static Vector2 getMeeplePosition(Vector2 tilePosition, ChunkId chunkId) {
        return meepleOffset.get(chunkId).add(tilePosition);
    }

    /**
     * Gets the sprite model to use for the specified player.
     *
     * @param index The index of the player in the game.
     * @return The sprite model to use for the specified player's meeple.
     */
    private static String getOwnMeepleSpriteModel(int index) {
        return Integer.toString(index);
    }

    /**
     * Gets the sprite model to use for the specified tile and rotation.
     *
     * @param tile The tile to get the sprite model for.
     * @return The sprite model to use for the specified tile and rotation.
     */
    private static String getTileSpriteModel(Tile tile) {
        return String.format("%s_%s", tile.getConfig().getModel(), tile.getRotation().ordinal() * TileRotation.ANGLE_ROTATION);
    }

    /**
     * Draw zone with a unique color
     *
     * @param g            The graphics to draw into
     * @param tilePosition The position of the tile associated with the chunk
     * @param chunk        The chunk of the chunk to draw
     * @param colorZone    The colors of all zone
     */
    private static void drawZone(Graphics g, Vector2 tilePosition, Chunk chunk, HashMap<Area, Color> colorZone) {
        Area area = chunk.getArea();

        if (!colorZone.containsKey(area))
            colorZone.put(area, generateColor(0.35f));

        g.setColor(colorZone.get(area));
        Polygon polygon = chunksGeo.get(chunk.getCurrentId());
        g.fillPolygon(polygon.getXs(tilePosition.x()), polygon.getYs(tilePosition.y()), polygon.getVectorCount());
    }

    /**
     * Draw on closed zone a closed pattern
     *
     * @param g            The graphics to draw into
     * @param tilePosition The position of the tile associated with the chunk
     * @param chunk        The chunk of the chunk to draw
     */
    private static void drawClosedZone(Graphics g, Vector2 tilePosition, Chunk chunk) {
        if (!chunk.getArea().isClosed())
            return;

        drawPattern(g, "PatternClosed", chunksGeo.get(chunk.getCurrentId()), tilePosition);
    }

    private static void drawPattern(Graphics g, String patternName, Polygon polygon, Vector2 tilePosition) {
        BufferedImage patternImage = extraDatabase.get(patternName);
        TexturePaint patternTexture = new TexturePaint(patternImage, new Rectangle(0, 0, patternImage.getWidth(), patternImage.getHeight()));
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(patternTexture);
        g2.fillPolygon(polygon.getXs(tilePosition.x()), polygon.getYs(tilePosition.y()), polygon.getVectorCount());
    }

    /**
     * Generate a random color
     *
     * @param opacity The opacity wanted (0-1)
     * @return a random color
     */
    public static Color generateColor(float opacity) {
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new Color(r, g, b, opacity);
    }

    /**
     * Draw tile border
     *
     * @param g            The graphics to draw into
     * @param tilePosition The position of the tile associated with the chunk
     */
    private static void drawTileBorder(Graphics g, Vector2 tilePosition) {
        g.setColor(Color.black);
        drawTileBorderUp(g, tilePosition);
        drawTileBorderDown(g, tilePosition);
        drawTileBorderLeft(g, tilePosition);
        drawTileBorderRight(g, tilePosition);
    }

    private static void drawTileBorderUp(Graphics g, Vector2 tilePosition) {
        g.drawLine(tilePosition.x(), tilePosition.y(), tilePosition.x() + TILE_WIDTH, tilePosition.y());
    }

    private static void drawTileBorderDown(Graphics g, Vector2 tilePosition) {
        g.drawLine(tilePosition.x(), tilePosition.y() + TILE_HEIGHT - 1, tilePosition.x() + TILE_WIDTH, tilePosition.y() + TILE_HEIGHT - 1);
    }

    private static void drawTileBorderLeft(Graphics g, Vector2 tilePosition) {
        g.drawLine(tilePosition.x(), tilePosition.y(), tilePosition.x(), tilePosition.y() + TILE_HEIGHT);
    }

    private static void drawTileBorderRight(Graphics g, Vector2 tilePosition) {
        g.drawLine(tilePosition.x() + TILE_WIDTH - 1, tilePosition.y(), tilePosition.x() + TILE_WIDTH - 1, tilePosition.y() + TILE_HEIGHT);
    }

    /**
     * Creates a new image layer representing the specified game instance.
     *
     * @param game The game instance to create the image layer for.
     * @return The image layer representing the specified game instance.
     */
    public static BufferedImage createLayer(Game game) {
        return createLayer(game, calculateBoardBounds(game.getBoard()));
    }

    /**
     * Creates a new image layer representing the specified game instance.
     *
     * @param game        The game instance to create the image layer for.
     * @param boardBounds The bounds of the board.
     * @return The image layer representing the specified game instance.
     */
    public static BufferedImage createLayer(Game game, Bounds boardBounds) {
        Bounds layerBounds = boardBounds.scale(TILE_WIDTH, TILE_HEIGHT).reverseY();
        BufferedImage layer = new BufferedImage(layerBounds.getWidth(), layerBounds.getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D layerGraphics = layer.createGraphics();
        render(game, layerGraphics, layerBounds);
        layerGraphics.dispose();

        return layer;
    }

    /**
     * Renders the specified game instance to the specified graphics context.
     *
     * @param game        The game instance to render.
     * @param graphics    The graphics context to render to.
     * @param layerBounds The bounds of the layer to render.
     */
    public static synchronized void render(Game game, Graphics graphics, Bounds layerBounds) {
        loadImageDatabaseIfNeeded();
        HashMap<Area, Color> colorZone = new HashMap<>();

        graphics.setFont(new Font("Courier New", Font.BOLD, 25));
        graphics.setColor(Color.blue);

        for (Tile tile : game.getBoard().getTiles()) {
            Vector2 tileImagePosition = getTilePosition(tile.getPosition()).reverseY().subtract(layerBounds.start);
            BufferedImage tileImage = tileDatabase.get(getTileSpriteModel(tile));

            assert tileImagePosition.x() >= 0 && tileImagePosition.y() >= 0;
            if (tileImagePosition.x() + TILE_WIDTH > layerBounds.getWidth() || tileImagePosition.y() + TILE_HEIGHT > layerBounds.getHeight())
                throw new AssertionError();

            graphics.drawImage(tileImage, tileImagePosition.x(), tileImagePosition.y(), null);
            graphics.drawString("%s %d %d".formatted(tile.getConfig().getModel(), tile.getPosition().x(), tile.getPosition().y()), tileImagePosition.x() + TILE_WIDTH / 4, tileImagePosition.y() + TILE_HEIGHT / 2);

            for (ChunkId chunkId : ChunkId.values()) {
                Chunk chunk = tile.getChunk(chunkId);

                // Show zones
                drawZone(graphics, tileImagePosition, chunk, colorZone);

                // Show closed zone
                drawClosedZone(graphics, tileImagePosition, chunk);

                // Show tile border
                drawTileBorder(graphics, tileImagePosition);

                if (chunk.hasMeeple()) {
                    Meeple meeple = chunk.getMeeple();
                    Vector2 meepleImagePosition = getMeeplePosition(tileImagePosition, chunkId);
                    BufferedImage meepleImage = meepleDatabase.get(getOwnMeepleSpriteModel(game.getPlayerIndex(meeple.getOwner())));
                    graphics.setColor(Color.blue);
                    graphics.drawImage(meepleImage, meepleImagePosition.x(), meepleImagePosition.y(), null);
                }
            }
        }

        GameBoard gameBoard = game.getBoard();
        if (gameBoard.hasDragon()) {
            Dragon dragon = gameBoard.getDragon();
            Vector2 dragonImagePosition = getTilePosition(dragon.getPosition()).reverseY().subtract(layerBounds.start);
            BufferedImage dragonImage = dragonDatabase.get("dragon");
            graphics.setColor(Color.blue);
            graphics.drawImage(dragonImage, dragonImagePosition.x(), dragonImagePosition.y(), null);
        }
    }
}
