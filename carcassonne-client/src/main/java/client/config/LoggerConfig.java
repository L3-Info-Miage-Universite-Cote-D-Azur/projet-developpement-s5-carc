package client.config;

import client.logger.LogLevel;
import excel.ExcelNode;

import java.net.URISyntaxException;
import java.nio.file.Path;

/**
 * Represents the configuration of the logger.
 * It contains the color for the foreground texts and if the logger should be enabled.
 */
public class LoggerConfig {
    private final LogLevel level;
    private final String debugColor;
    private final String infoColor;
    private final String warningColor;
    private final String errorColor;

    public LoggerConfig(LogLevel level, String debugColor, String infoColor, String warningColor, String errorColor) {
        this.level = level;
        this.debugColor = debugColor;
        this.infoColor = infoColor;
        this.warningColor = warningColor;
        this.errorColor = errorColor;
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getDebugColor() {
        return debugColor;
    }

    public String getInfoColor() {
        return infoColor;
    }

    public String getWarningColor() {
        return warningColor;
    }

    public String getErrorColor() {
        return errorColor;
    }

    public static LoggerConfig getDefaultConfig() {
        return new LoggerConfig(LogLevel.INFO, "36m", "32m", "33m", "31m");
    }

    public static LoggerConfig loadFromResources() {
        try {
            ExcelNode config = ExcelNode.load(Path.of(LoggerConfig.class.getResource("logger.txt").toURI()));

            return new LoggerConfig(LogLevel.valueOf(config.getRow("Level").getValue("Value")),
                    config.getRow("Debug").getValue("Value"),
                    config.getRow("Info").getValue("Value"),
                    config.getRow("Warning").getValue("Value"),
                    config.getRow("Error").getValue("Value"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
