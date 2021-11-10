package Utils;

import logger.Logger;
import logic.Game;
import logic.board.GameBoard;
import logic.math.Vector2;
import logic.tile.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The output utils
 */
public interface OutputUtils {
    /**
     * Align a string to the center
     *
     * @param word    The word to align to the center
     * @param maxSize The max size that can be used to center
     * @return The word align to the center
     */
    static String alignToCenter(String word, int maxSize) {
        StringBuilder wordBuilder = new StringBuilder(word);
        int size = (maxSize - word.length()) / 2;
        for (int j = 0; j < size; j++)
            wordBuilder.insert(0, " ").append(" ");

        if (wordBuilder.toString().length() % 2 != 0)
            wordBuilder.append(" ");

        wordBuilder.append("|");
        return wordBuilder.toString();
    }

    /**
     * Add a line (array) and fill with a key and these values
     *
     * @param lineName      The line name
     * @param cellSize      The max size that can be used to center
     * @param values        The values to fill into the line
     * @param stringBuilder The stringbuilder of the array
     */
    static void toArrayLine(String lineName, int cellSize, List<String> values, StringBuilder stringBuilder) {
        stringBuilder.append("|").append(OutputUtils.alignToCenter(lineName, cellSize));
        for (String value : values)
            stringBuilder.append(OutputUtils.alignToCenter(value, cellSize));
        stringBuilder.append("\n");
    }

    static void gameToImage(Game game) {
        int imgSize = 160;
        int finalSize = imgSize * GameBoardUtils.maxSizeBoard(game.getBoard());
        BufferedImage bufferedImage = new BufferedImage(finalSize, finalSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        BufferedImage img = null;
        for (Tile tile : game.getBoard().getTiles()) {
            try {
                img = ImageIO.read(new File("models/" + tile.getData().model + ".png"));
            } catch (IOException e) {
                Logger.error(e.getMessage());
                return;
            }

            Vector2 positionOnImage = GameBoardUtils.getCoordinateImage(tile.getPosition(), finalSize, imgSize);
            g2d.drawImage(img, positionOnImage.getX(), positionOnImage.getY(), null);
            Vector2 positionOnImageNoCenter = GameBoardUtils.getCoordinateImageNoCenter(tile.getPosition(), finalSize, imgSize);
            Font myFont = new Font ("Courier New", 1, 35);
            g2d.setFont (myFont);
            g2d.setColor(Color.black);
            g2d.drawString(tile.getPosition().getX() + " " + tile.getPosition().getY(), positionOnImageNoCenter.getX() ,positionOnImageNoCenter.getY());
        }

        g2d.dispose();
        File file = new File("GameState.png");
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            Logger.error(e.getMessage());
            return;
        }
    }
}
