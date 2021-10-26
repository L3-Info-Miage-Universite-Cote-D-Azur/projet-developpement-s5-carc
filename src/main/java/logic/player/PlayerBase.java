package logic.player;

import logic.Game;

public abstract class PlayerBase {
    protected final int id;
    protected int score;

    protected Game game;

    public PlayerBase(int id) {
        this.id = id;
    }

    public final int getId() {
        return id;
    }

    public final Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public final int getScore() {
        return score;
    }

    public void addScore(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Score to be added must be positive.");
        }

        score += value;
    }

    public abstract void onTurn();
}
