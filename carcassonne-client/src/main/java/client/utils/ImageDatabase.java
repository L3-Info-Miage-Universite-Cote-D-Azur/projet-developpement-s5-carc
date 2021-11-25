package client.utils;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Database of images.
 * Provides a way to get images from the memory-database.
 */
public class ImageDatabase {
    private final int width;
    private final int height;
    private Map<String, BufferedImage> assets;

    public ImageDatabase(int width, int height) {
        this.width = width;
        this.height = height;
        assets = new HashMap<>();
    }

    /**
     * Adds an image to the database.
     *
     * @param assetName The name of the image.
     * @param image     The image.
     */
    public void cache(String assetName, BufferedImage image) {
        if (image.getWidth() != width || image.getHeight() != height) {
            throw new IllegalArgumentException("Image " + assetName + " is not of the correct size");
        }

        assets.put(assetName, image);
    }

    /**
     * Gets an image from the database.
     *
     * @param assetName The name of the image.
     * @return The image.
     */
    public BufferedImage get(String assetName) {
        if (!assets.containsKey(assetName)) {
            throw new IllegalArgumentException("Image " + assetName + " is not in the database");
        }

        return assets.get(assetName);
    }

    /**
     * Flushes the database.
     */
    public void flush() {
        for (BufferedImage image : assets.values()) {
            image.flush();
        }

        assets.clear();
    }

    /**
     * Gets the width of the images.
     *
     * @return The width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the images.
     *
     * @return The height.
     */
    public int getHeight() {
        return height;
    }
}
