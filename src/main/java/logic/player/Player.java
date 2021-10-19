package logic.player;

import input.PlayerInput;
import logic.Game;

public class Player {
    private final PlayerInfo info;
    private final PlayerInput input;
    private final Game game;

    public Player(PlayerInfo info, PlayerInput input, Game game) {
        this.info = info;
        this.input = input;
        this.game = game;
    }

    public void onTurn() {
        input.onTurn();
    }
}
