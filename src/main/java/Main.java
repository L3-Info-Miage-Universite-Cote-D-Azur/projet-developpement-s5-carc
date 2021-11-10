import Utils.OutputUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import logger.Logger;
import logic.Game;
import logic.config.GameConfig;
import logic.player.SimpleAIPlayer;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.nio.file.Paths;

import static java.lang.System.*;

public class Main {

    public static void main(String[] arg) throws ConfigurationException {
        Logger.enable();
        GameConfig config = loadConfigFromFile("config.json");

        if (config == null || !config.validate()) {
            throw new ConfigurationException("Configuration is not valid.");
        }


        playSingleGame(config, 5);
        // playMultipleGames(config, 5, 2);
    }

    private static GameConfig loadConfigFromFile(String filename) {
        try {
            return new ObjectMapper().readValue(Paths.get(filename).toFile(), GameConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void playSingleGame(GameConfig config, int numPlayers) {
        Game game = new Game(config);

        for (int i = 0; i < numPlayers; i++) {
            game.addPlayer(new SimpleAIPlayer(i + 1));
        }

        game.start();
        game.updateToEnd();
        out.println(game.getStat(20));
        OutputUtils.gameToImage(game);
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
        out.println(game.getStat(20));
    }
}
