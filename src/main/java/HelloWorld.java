import logic.Game;
import logic.config.GameConfig;

import java.io.IOException;

public class HelloWorld {
    public static void main(String[] arg) {
        System.out.println("Hello World !");
        System.out.println(new GameConfig().toString());
    }
}
