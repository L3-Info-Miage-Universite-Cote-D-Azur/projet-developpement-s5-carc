package client.utils;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ImageDatabase {
    private Map<String, BufferedImage> assets;
    private final int width;
    private final int height;

    public ImageDatabase(int width, int height) {
        this.width = width;
        this.height = height;
        assets = new HashMap<>();
    }

    public void cache(String assetName, BufferedImage image) {
        if (image.getWidth() != width || image.getHeight() != height) {
            throw new IllegalArgumentException("Image " + assetName + " is not of the correct size");
        }

        assets.put(assetName, image);
    }

    public BufferedImage get(String assetName) {
        return assets.get(assetName);
    }

    public void flush() {
        for (BufferedImage image : assets.values()) {
            image.flush();
        }

        assets.clear();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
