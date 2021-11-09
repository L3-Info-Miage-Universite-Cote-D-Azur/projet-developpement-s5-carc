package logic.tile;

import logic.player.PlayerBase;

public class MeeplesPlacement {
    private boolean meeple;
    private Chunk chunk;
    private PlayerBase player;

    public MeeplesPlacement(boolean meeple, Chunk chunk, PlayerBase player) {
        this.meeple = meeple;
        this.chunk = chunk;
        this.player = player;
    }

    public boolean getMeeple() { return meeple; }

    public Chunk getChunk() { return chunk; }
    
    public PlayerBase getPlayer() { return player; }


/*
Verifier présence de tuile avec l'utilisation des vecteurs

Si on place un meeple c'est boolean car on peut avoir soit un meeple exemple
meeple.yellow = 0
meeple.red = 1
meeple.green = 0

récuperer vecteur 1 du tile


récuperer vecteur 2 du coté du tile
 */
}
