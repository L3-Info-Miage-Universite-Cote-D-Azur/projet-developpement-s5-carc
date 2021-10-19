package input;

import logic.Game;

public interface PlayerInput {
    void onTurn();
    void setGame(Game game);
}