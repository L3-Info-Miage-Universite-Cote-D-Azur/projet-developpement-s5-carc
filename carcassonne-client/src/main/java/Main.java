import com.fasterxml.jackson.databind.ObjectMapper;
import logic.Game;
import logic.config.GameConfig;
import logic.player.SimpleAIPlayer;
import utils.GameDrawUtils;
import utils.GameScoreUtils;

import javax.imageio.ImageIO;
import javax.naming.ConfigurationException;
import java.io.IOException;
import java.nio.file.Paths;

import static java.lang.System.*;

public class Main {
    public static void main(String[] arg) throws ConfigurationException {
        GameConfig config = GameConfig.loadFromDirectory("config");

        if (config == null || !config.validate()) {
            throw new ConfigurationException("Configuration is not valid.");
        }

        playSingleGame(config, 5);
        // playMultipleGames(config, 5, 2);
    }

    private static void playSingleGame(GameConfig config, int numPlayers) {
        Game game = new Game(config);

        for (int i = 0; i < numPlayers; i++) {
            game.addPlayer(new SimpleAIPlayer(i + 1));
        }

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
            game.addPlayer(new SimpleAIPlayer(i + 1));
        }

        for (int i = 0; i < gameCount; i++) {
            game.start();
            game.updateToEnd();
        }
        out.println(GameScoreUtils.createScoreTable(game, 20));
    }
}