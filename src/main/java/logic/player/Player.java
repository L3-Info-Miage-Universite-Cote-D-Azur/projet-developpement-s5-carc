package logic.player;

import input.PlayerInput;

public class Player {
    private final PlayerInfo info;
    private final PlayerInput input;

    public Player(PlayerInfo info, PlayerInput input) {
        this.info = info;
        this.input = input;
    }

    public void onTurn() {
        input.onTurn();
    }
}
