package logic.player;

import input.PlayerInput;
import logic.tile.TileDeck;

public class Player {
    private final PlayerInfo info;
    private final PlayerInput input;
    private final TileDeck deck;

    public Player(PlayerInfo info, PlayerInput input) {
        this.info = info;
        this.input = input;
        this.deck = new TileDeck();
    }

    public void onTurn() {
        input.onTurn();
    }
}
