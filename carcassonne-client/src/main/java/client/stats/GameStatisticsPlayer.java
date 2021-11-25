package client.stats;

import logic.player.Player;

/**
 * Represents the statistics of a player.
 */
public class GameStatisticsPlayer {
    private final int id;
    private final int roadScore;
    private final int townScore;
    private final int abbeyScore;
    private final int fieldScore;
    private final int remainingMeeples;
    private final int playedMeeples;

    private final int order;

    public GameStatisticsPlayer(Player player, int order) {
        this.id = player.getId();
        this.roadScore = player.getRoadScore();
        this.townScore = player.getTownScore();
        this.abbeyScore = player.getAbbeyScore();
        this.fieldScore = player.getFieldPoints();
        this.remainingMeeples = player.getMeeplesRemained();
        this.playedMeeples = player.getMeeplesPlayed();
        this.order = order;
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
     * Gets the player's order in the ranking.
     *
     * @return the player's order in the ranking.
     */
    public int getOrder() {
        return order;
    }
}
