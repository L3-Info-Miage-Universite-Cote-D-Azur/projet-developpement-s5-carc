package logic.player;

import logic.Game;
import logic.tile.chunk.ChunkType;
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
            public void onWaitingMeeplePlacement() {

            }

            @Override
            public void onWaitingDragonMove() {

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
            public void onWaitingMeeplePlacement() {

            }

            @Override
            public void onWaitingDragonMove() {

            }
        };
    }

    /**
     * Initializes the player to the default state.
     */
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
     *
     * @return the player's listener
     */
    public IPlayerListener getListener() {
        return listener;
    }

    /**
     * Set the player's listener
     *
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
     * Add a score to a specific chunk type.
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

    /**
     * Gets the road score
     *
     * @return the road score
     */
    public int getRoadScore() {
        return roadScore;
    }

    /**
     * Gets the town score
     *
     * @return the town score
     */
    public int getTownScore() {
        return townScore;
    }

    /**
     * Gets the abbey score
     *
     * @return the abbey score
     */
    public int getAbbeyScore() {
        return abbeyScore;
    }

    /**
     * Gets the field score
     *
     * @return the field score
     */
    public int getFieldPoints() {
        return fieldScore;
    }

    /**
     * Gets the meeples played
     *
     * @return the meeples played
     */
    public int getMeeplesPlayed() {
        return meeplesPlayed;
    }

    /**
     * Decrements the meeples played.
     */
    public void decreasePlayedMeeples() {
        meeplesPlayed--;
    }

    /**
     * Increments the meeples played.
     */
    public void increasePlayedMeeples() {
        meeplesPlayed++;
    }

    /**
     * Gets the meeples left
     *
     * @return the meeples left
     */
    public int getMeeplesRemained() {
        if (game == null) {
            return 0;
        }
        return game.getConfig().startingMeepleCount - meeplesPlayed;
    }

    /**
     * Gets whether there are meeples left
     *
     * @return true if there are meeples left, false otherwise
     */
    public boolean hasRemainingMeeples() {
        return getMeeplesRemained() >= 1;
    }

    /**
     * Encodes the player's attributes into the given output stream.
     *
     * @param stream the output stream
     */
    public void encode(ByteOutputStream stream) {
        stream.writeInt(id);
        stream.writeInt(roadScore);
        stream.writeInt(townScore);
        stream.writeInt(abbeyScore);
        stream.writeInt(fieldScore);
        stream.writeInt(meeplesPlayed);
    }

    /**
     * Decodes the player's attributes from the given input stream.
     *
     * @param stream the input stream
     */
    public void decode(ByteInputStream stream) {
        id = stream.readInt();
        roadScore = stream.readInt();
        townScore = stream.readInt();
        abbeyScore = stream.readInt();
        fieldScore = stream.readInt();
        meeplesPlayed = stream.readInt();
    }
}
