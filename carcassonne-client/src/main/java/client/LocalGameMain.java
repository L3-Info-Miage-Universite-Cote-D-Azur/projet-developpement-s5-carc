package client;

import client.ai.SimpleAI;
import client.command.CommandLogger;
import client.config.LoggerConfig;
import client.listener.GameLogger;
import client.logger.Logger;
import logic.Game;
import logic.IGameListener;
import logic.config.GameConfig;
import logic.player.Player;
import logic.tile.ChunkId;
import logic.tile.Tile;
import client.utils.GameDrawUtils;
import client.utils.GameScoreUtils;

import javax.imageio.ImageIO;
import javax.naming.ConfigurationException;
import java.io.IOException;

import static java.lang.System.out;

public class LocalGameMain {
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

        game.setListener(new GameLogger());
        game.getCommandExecutor().setListener(new CommandLogger());
        game.start();

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
        }
        out.println(GameScoreUtils.createScoreTable(game, 20));
    }
}
