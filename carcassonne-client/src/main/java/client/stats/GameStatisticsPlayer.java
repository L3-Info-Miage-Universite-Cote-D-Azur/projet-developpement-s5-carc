package client.stats;

import logic.player.Player;

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

    public int getId() {
        return id;
    }

    public int getTotalScore() {
        return roadScore + townScore + abbeyScore + fieldScore;
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

    public int getFieldScore() {
        return fieldScore;
    }

    public int getRemainingMeeples() {
        return remainingMeeples;
    }

    public int getPlayedMeeples() {
        return playedMeeples;
    }

    public int getOrder() {
        return order;
    }
}
