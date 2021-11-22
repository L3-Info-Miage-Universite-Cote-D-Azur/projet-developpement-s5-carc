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

    public LoggerConfig(ExcelNode node) {
        this.level = LogLevel.valueOf(node.getRow("Level").getValue("Value"));
        this.debugColor = node.getRow("Debug").getValue("Value");
        this.infoColor = node.getRow("Info").getValue("Value");
        this.warningColor = node.getRow("Warning").getValue("Value");
        this.errorColor = node.getRow("Error").getValue("Value");
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
}
