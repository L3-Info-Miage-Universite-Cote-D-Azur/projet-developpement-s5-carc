package logic.player;

import input.PlayerInput;

public class Player {
    private PlayerInfo info;
    private PlayerInput input;

    public Player(PlayerInfo info, PlayerInput input) {
        this.info = info;
        this.input = input;
    }

    public void onTurn() {
        input.onTurn();
    }
}
