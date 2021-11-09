package logic;

import logger.Logger;
import logic.player.PlayerBase;

public class GameTurn {
    private final Game game;
    private int id;

    public GameTurn(Game game) {
        this.game = game;
        this.id = 0;
    }

    public void reset() {
        id = 0;
    }

    public void playTurn() {
        this.id++;
        Logger.info(String.format("[GAME] Turn %d -> Player %d", id, getPlayer().getId()));
        this.getPlayer().onTurn();
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
