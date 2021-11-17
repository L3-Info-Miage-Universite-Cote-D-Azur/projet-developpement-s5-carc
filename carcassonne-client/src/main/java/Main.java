import ai.SimpleAI;
import config.LoggerConfig;
import logger.Logger;
import logic.Game;
import logic.IGameListener;
import logic.config.GameConfig;
import logic.player.Player;
import logic.tile.ChunkId;
import logic.tile.Tile;
import utils.GameDrawUtils;
import utils.GameScoreUtils;

import javax.imageio.ImageIO;
import javax.naming.ConfigurationException;
import java.io.IOException;
import java.nio.file.Paths;

import static java.lang.System.*;

public class Main {
    public static void main(String[] arg) throws ConfigurationException {
        Logger.setConfig(LoggerConfig.loadFromResources());
        GameConfig config = GameConfig.loadFromResources();

        if (!config.validate()) {
            throw new ConfigurationException("Configuration is not valid.");
        }

        playSingleGame(config, 5);
        // playMultipleGames(config, 5, 2);
    }

    private static void playSingleGame(GameConfig config, int numPlayers) {
        Game game = new Game(config);

        for (int i = 0; i < numPlayers; i++) {
            game.addPlayer(new Player(i + 1) {{
                setListener(new SimpleAI(this));
            }});
        }

        game.setListener(new IGameListener() {
            @Override
            public void onTurnStarted(int id) {
                Logger.info("--- Turn %d started. ---", id);
            }

            @Override
            public void onTurnEnded(int id) {
                Logger.info("--- Turn %d ended. ---", id);
            }

            @Override
            public void onTilePlaced(Tile tile) {
                Logger.info("Place tile %s at (%d,%d)", tile, tile.getPosition().getX(), tile.getPosition().getY());
            }

            @Override
            public void onMeeplePlaced(Player player, Tile tile, ChunkId chunkId) {
                Logger.info("Place meeple of player %d at tile (%d,%d), chunk %s", player.getId(), tile.getPosition().getX(), tile.getPosition().getY(), chunkId);
            }

            @Override
            public void onMeepleRemoved(Player player, Tile tile, ChunkId chunkId) {
                Logger.info("Meeple of player %d at tile (%d,%d), chunk %s is removed", player.getId(), tile.getPosition().getX(), tile.getPosition().getY(), chunkId);
            }

            @Override
            public void onStart() {
                Logger.info("--- GAME START ---");
            }

            @Override
            public void onEnd() {
                Logger.info("--- GAME OVER ---");
            }

            @Override
            public void onCommandFailed(String reason) {
                Logger.warn("Command execution failed: " + reason);
            }

            @Override
            public void onCommandFailed(String reason, Object... args) {
                Logger.warn("Command execution failed: " + reason, args);
            }
        });

        game.start();
        game.updateToEnd();

        out.println(GameScoreUtils.createScoreTable(game, 20));

        try {
            ImageIO.write(GameDrawUtils.createLayer(game), "png", new java.io.File("game.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void playMultipleGames(GameConfig config, int numPlayers, int gameCount) {
        Game game = new Game(config);

        for (int i = 0; i < numPlayers; i++) {
            game.addPlayer(new Player(i + 1) {{
                setListener(new SimpleAI(this));
            }});
        }

        for (int i = 0; i < gameCount; i++) {
            game.start();
            game.updateToEnd();
        }
        out.println(GameScoreUtils.createScoreTable(game, 20));
    }
}