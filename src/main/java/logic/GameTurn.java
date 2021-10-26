package logic;

import logic.player.PlayerBase;

public class GameTurn {
    private final Game game;
    private int id;

    public GameTurn(Game game) {
        this.game = game;
        this.id = 0;
    }

    public void startNext() {
        this.id++;
    }

    public int getPlayerIndex() {
        return (this.id - 1) % game.getPlayerCount();
    }

    public PlayerBase getPlayer() {
        return game.getPlayer(getPlayerIndex());
    }

    public int getCount() {
        return this.id;
    }
}
