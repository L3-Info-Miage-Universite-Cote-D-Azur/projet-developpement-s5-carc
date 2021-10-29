import logic.Game;
import logic.config.GameConfig;
import logic.player.PlayerBase;
import logic.player.SimpleAIPlayer;

import static java.lang.System.*;

public class Main {

    public static void main(String[] arg) {
        Game game = new Game(new GameConfig());

        game.createPlayer(new SimpleAIPlayer(1));
        game.createPlayer(new SimpleAIPlayer(2));
        game.createPlayer(new SimpleAIPlayer(3));
        game.createPlayer(new SimpleAIPlayer(4));
        game.createPlayer(new SimpleAIPlayer(5));

        game.start();
        /*for (int i = 0; i < game.getPlayerCount(); i++) {
           game.getPlayer(i).addScore(100);
        }

        game.getPlayer(2).addScore(20);
        game.getPlayer(1).addScore(20);*/

        while (!game.isFinished()) {
            game.update();
            out.println(game.getBoard().toString());
        }
        /*for (int i = 0; i < game.getPlayerCount(); i++) {
            PlayerBase playerBase = game.getPlayer(i);
            System.out.println("Player score " + playerBase.getId() + " : " + playerBase.getScore());
        }*/
    }
}
