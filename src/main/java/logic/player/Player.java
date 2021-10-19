package logic.player;

import input.PlayerInput;
import logic.Game;

public class Player {
    private final PlayerInfo info;
    private final PlayerInput input;
    private final Game game;
    private int score;


    public Player(PlayerInfo info, PlayerInput input, Game game) {
        this.info = info;
        this.input = input;
        this.game = game;
        this.input.setPlayer(this);
    }

    public void onTurn() {
        input.onTurn();
    }

    public int getScore() {
        return score;
    }

    public void addScore(int value) {
        this.score += value;
    }

    public PlayerInfo getInfo() {
        return info;
    }

    public Game getGame() {
        return game;
    }
}
