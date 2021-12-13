package client.stats;

import logic.player.Player;

/**
 * Represents the statistics of a player.
 */
public class GameStatisticsPlayer implements Comparable<GameStatisticsPlayer> {
    private final int id;
    private int roadScore;
    private int townScore;
    private int abbeyScore;
    private int fieldScore;
    private int remainingMeeples;
    private int playedMeeples;

    public GameStatisticsPlayer(Player player) {
        this.id = player.getId();
        this.append(player);
    }

    /**
     * Appends the player's statistics.
     *
     * @param player the player to append.
     */
    public void append(Player player) {
        this.roadScore += player.getRoadScore();
        this.townScore += player.getTownScore();
        this.abbeyScore += player.getAbbeyScore();
        this.fieldScore += player.getFieldPoints();
        this.remainingMeeples += player.getMeeplesRemained();
        this.playedMeeples += player.getMeeplesPlayed();
    }

    /**
     * Gets the player's id.
     *
     * @return the player's id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the player's total score.
     *
     * @return the player's total score.
     */
    public int getTotalScore() {
        return roadScore + townScore + abbeyScore + fieldScore;
    }

    /**
     * Gets the player's road score.
     *
     * @return the player's road score.
     */
    public int getRoadScore() {
        return roadScore;
    }

    /**
     * Gets the player's town score.
     *
     * @return the player's town score.
     */
    public int getTownScore() {
        return townScore;
    }

    /**
     * Gets the player's abbey score.
     *
     * @return the player's abbey score.
     */
    public int getAbbeyScore() {
        return abbeyScore;
    }

    /**
     * Gets the player's field score.
     *
     * @return the player's field score.
     */
    public int getFieldScore() {
        return fieldScore;
    }

    /**
     * Gets the player's remaining meeples.
     *
     * @return the player's remaining meeples.
     */
    public int getRemainingMeeples() {
        return remainingMeeples;
    }

    /**
     * Gets the player's played meeples.
     *
     * @return the player's played meeples.
     */
    public int getPlayedMeeples() {
        return playedMeeples;
    }

    /**
     * Compares this player to another player.
     *
     * @param other the other player.
     * @return the comparison result.
     */
    @Override
    public int compareTo(GameStatisticsPlayer other) {
        if (other == null) {
            throw new NullPointerException("The other player cannot be null.");
        }
        return Integer.compare(getTotalScore(), other.getTotalScore());
    }
}
