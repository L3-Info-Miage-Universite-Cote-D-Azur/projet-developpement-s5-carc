package client.config;

import client.logger.LogLevel;
import excel.ExcelNode;
import excel.ExcelRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the configuration of the logger.
 * It contains the color for the foreground texts and if the logger should be enabled.
 */
public class LoggerConfig {
    private final LogLevel level;
    private final List<String> playerColors;

    public LoggerConfig(LogLevel level, List<String> playerColors) {
        this.level = level;
        this.playerColors = playerColors;
    }

    public LoggerConfig(ExcelNode node) {
        this.level = LogLevel.valueOf(node.getRow("Level").getValue("Value"));

        ArrayList<String> playerColors = new ArrayList<>();

        for (int i = 1; ; i++) {
            ExcelRow playerColor = node.getRow("Player_" + i);

            if (playerColor != null) {
                playerColors.add(playerColor.getValue("Value"));
            } else {
                break;
            }
        }

        this.playerColors = playerColors;
    }

    /**
     * Gets the default configuration.
     *
     * @return the default configuration
     */
    public static LoggerConfig getDefaultConfig() {
        return new LoggerConfig(LogLevel.INFO, new ArrayList<>() {{
            add("91m");
            add("92m");
            add("93m");
            add("94m");
            add("95m");
        }});
    }

    /**
     * Gets the level of the logger.
     * If the log level is lower than the logger level, the log will be ignored.
     *
     * @return
     */
    public LogLevel getLevel() {
        return level;
    }

    /**
     * Gets the color for the given player.
     *
     * @param index the player index in the game
     * @return the color as ANSI escape code
     */
    public String getPlayerColor(int index) {
        return playerColors.get(index);
    }

    /**
     * Gets the color for a debug log.
     *
     * @return the color as ANSI escape code
     */
    public String getDebugColor() {
        return "32m";
    }

    /**
     * Gets the color for a info log.
     *
     * @return the color as ANSI escape code
     */
    public String getInfoColor() {
        return "32m";
    }

    /**
     * Gets the color for a warning log.
     *
     * @return the color as ANSI escape code
     */
    public String getWarningColor() {
        return "33m";
    }

    /**
     * Gets the color for a error log.
     *
     * @return the color as ANSI escape code
     */
    public String getErrorColor() {
        return "31m";
    }
}
