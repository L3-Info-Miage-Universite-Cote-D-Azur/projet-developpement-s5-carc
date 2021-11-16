package logic.player;

import logic.Game;
import logic.tile.ChunkType;

/**
 * The base player
 */
public class Player implements Comparable {
    protected final int id;
    protected int roadScore;
    protected int townScore;
    protected int abbeyScore;
    protected int fieldScore;
    protected int remainingMeepleCount;

    protected Game game;
    protected IPlayerListener listener;

    /**
     * @param id The player id
     */
    public Player(int id) {
        this.id = id;
        this.listener = new IPlayerListener() {
            @Override
            public void play() {

            }
        };
    }

    public void init() {
        roadScore = 0;
        townScore = 0;
        abbeyScore = 0;
        fieldScore = 0;
        remainingMeepleCount = game.getConfig().startingMeepleCount;
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

    /**
     * Get the player's listener
     * @return the player's listener
     */
    public IPlayerListener getListener() {
        return listener;
    }

    /**
     * Set the player's listener
     * @param listener the new listener
     */
    public void setListener(IPlayerListener listener) {
        this.listener = listener;
    }

    /**
     * Get the total score of the player
     *
     * @return the total score
     */
    public final int getScore() {
        return roadScore + townScore + abbeyScore + fieldScore;
    }

    /**
     * Add a score to a scpecific chunk type
     *
     * @param value     The score to add
     * @param chunkType The chunk type
     * @throws IllegalArgumentException If the value to add is negative
     * @throws IllegalStateException    If the score to add cannot be added into the target Chunk Type
     */
    public void addScore(int value, ChunkType chunkType) {
        if (value < 0) {
            throw new IllegalArgumentException("Score to be added must be positive.");
        }

        switch (chunkType) {
            case ROAD -> roadScore += value;
            case TOWN -> townScore += value;
            case ABBEY -> abbeyScore += value;
            case FIELD -> fieldScore += value;
            default -> throw new IllegalStateException("Unexpected value: " + chunkType);
        }
    }

    /**
     * Compare the player by the score (Low to High). Compares this object with the specified object for order.
     *
     * @param compareTo The other player to compare to
     * @return returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    public int compareTo(Object compareTo) {
        Player compareToEmp = (Player) compareTo;
        return Integer.compare(id, compareToEmp.getScore());
    }

    public int getRoadPoints() {
        return roadScore;
    }

    public int getTownPoints() {
        return townScore;
    }

    public int getAbbeyPoints() {
        return abbeyScore;
    }

    public int getFieldPoints() {
        return fieldScore;
    }

    public int getRemainingMeepleCount() {
        return remainingMeepleCount;
    }

    public void removeRemainingMeepleCount() {
        remainingMeepleCount--;
    }

    public int getPartisansPlayed() {
        return 0;
    }

    public int getPartisansRemained() {
        return 0;
    }
}
