package client.view;

import client.logger.Logger;
import client.logger.LoggerCategory;
import client.view.GameBoardViewBuilder;
import logic.Game;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * Represents the view of the game board.
 */
public class GameBoardView {
    /**
     * The board view generated.
     */
    private final RenderedImage boardView;

    public GameBoardView(Game game) {
        boardView = GameBoardViewBuilder.createLayer(game);
    }

    /**
     * Saves the board view to a file.
     * @param file The file to save the board view to.
     */
    public void save(File file) {
        try {
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                FileChannel channel = fileOutputStream.getChannel();
                FileLock lock = channel.lock();

                try {
                    ImageIO.write(boardView, "jpg", fileOutputStream);
                } finally {
                    lock.release();
                }
            }
        } catch (IOException e) {
            Logger.error(LoggerCategory.SERVICE, "Failed to save the game statistics. %s", e);
        }
    }
}
