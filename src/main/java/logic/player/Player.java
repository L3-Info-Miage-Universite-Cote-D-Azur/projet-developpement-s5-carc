package logic.player;

import input.PlayerInput;
import logic.Game;

public abstract class Player {
    private final PlayerInfo info;
    private final PlayerInput input;
    private final Game game;
    private int score;

    public Player(PlayerInfo info, PlayerInput input, Game game) {
        this.info = info;
        this.input = input;
        this.game = game;
        this.score = score;
    }

    public void onTurn() {
        input.onTurn();
    }

    public int getScore() {
        return score;
    }

    public int addScore() {
        this.score = score;
        return score;
    }
}
