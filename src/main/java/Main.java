import logic.Game;
import logic.config.GameConfig;
import logic.player.SimpleAIPlayer;

public class Main {
    public static void main(String[] arg) {
        Game game = new Game(new GameConfig());

        game.createPlayer(new SimpleAIPlayer(1));
        game.createPlayer(new SimpleAIPlayer(2));
        game.createPlayer(new SimpleAIPlayer(3));
        game.createPlayer(new SimpleAIPlayer(4));
        game.createPlayer(new SimpleAIPlayer(5));

        game.start();

        while (!game.isFinished()) {
            game.update();
        }
    }
}
