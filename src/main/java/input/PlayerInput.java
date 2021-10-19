package input;

import logic.player.Player;

public interface PlayerInput {
    void onTurn();
    void setPlayer(Player player);
}