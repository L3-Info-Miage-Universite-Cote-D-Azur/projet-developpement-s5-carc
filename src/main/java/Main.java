import input.ai.SimpleAI;
import logic.Game;
import logic.config.GameConfig;
import logic.player.Player;
import logic.player.PlayerInfo;

import java.io.IOException;

public class Main {
    public static void main(String[] arg) {
        Game game = new Game(new GameConfig());

        game.createPlayer(new PlayerInfo(1), new SimpleAI());
        game.createPlayer(new PlayerInfo(2), new SimpleAI());
        game.createPlayer(new PlayerInfo(3), new SimpleAI());
        game.createPlayer(new PlayerInfo(4), new SimpleAI());
        game.createPlayer(new PlayerInfo(5), new SimpleAI());

        game.start();

        while (!game.isFinished()) {
            game.update();
        }
    }
}
