package logic.player;

import logic.Game;
import logic.tile.ChunkType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * The Player class represents the player in the game.
 */
public class Player implements Comparable {
    protected int id;
    protected int roadScore;
    protected int townScore;
    protected int abbeyScore;
    protected int fieldScore;
    protected int meeplesPlayed;

    protected Game game;
    protected IPlayerListener listener;

    public Player() {
        this.listener = new IPlayerListener() {
            @Override
            public void onWaitingPlaceTile() {

            }

            @Override
            public void onWaitingExtraAction() {

            }
        };
    }

    /**
     * @param id The player id
     */
    public Player(int id) {
        this.id = id;
        this.listener = new IPlayerListener() {
            @Override
            public void onWaitingPlaceTile() {

            }

            @Override
            public void onWaitingExtraAction() {

            }
        };
    }

    public void init() {
        roadScore = 0;
        townScore = 0;
        abbeyScore = 0;
        fieldScore = 0;
        meeplesPlayed = 0;
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
            case ROAD, ROAD_END -> roadScore += value;
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
        if (compareTo instanceof Player) {
            Player other = (Player) compareTo;
            return getScore() - other.getScore();
        } else {
            throw new IllegalArgumentException("Cannot compare to an object that is not a Player.");
        }
    }

    public int getRoadScore() {
        return roadScore;
    }

    public int getTownScore() {
        return townScore;
    }

    public int getAbbeyScore() {
        return abbeyScore;
    }

    public int getFieldPoints() {
        return fieldScore;
    }

    public int getMeeplesPlayed() {
        return meeplesPlayed;
    }

    public void decreasePlayedMeeples() {
        meeplesPlayed--;
    }
    public void increasePlayedMeeples() {
        meeplesPlayed++;
    }

    public int getMeeplesRemained() {
        return game.getConfig().startingMeepleCount - meeplesPlayed;
    }

    public boolean hasRemainingMeeples() {
        return getMeeplesRemained() >= 1;
    }

    public void encode(ByteOutputStream stream) {
        stream.writeInt(id);
        stream.writeInt(roadScore);
        stream.writeInt(townScore);
        stream.writeInt(abbeyScore);
        stream.writeInt(fieldScore);
        stream.writeInt(meeplesPlayed);
    }

    public void decode(ByteInputStream stream) {
        id = stream.readInt();
        roadScore = stream.readInt();
        townScore = stream.readInt();
        abbeyScore = stream.readInt();
        fieldScore = stream.readInt();
        meeplesPlayed = stream.readInt();
    }
}
