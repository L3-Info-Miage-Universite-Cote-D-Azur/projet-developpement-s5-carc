package client.config;

import excel.ExcelNode;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Represents the configuration of the logger.
 * It contains the color for the foreground texts and if the logger should be enabled.
 */
public class LoggerConfig {
    private final boolean enabled;
    private final String infoColor;
    private final String warningColor;
    private final String errorColor;

    public LoggerConfig(boolean enabled, String infoColor, String warningColor, String errorColor) {
        this.enabled = enabled;
        this.infoColor = infoColor;
        this.warningColor = warningColor;
        this.errorColor = errorColor;
    }

    public boolean isEnabled() {
        return enabled;
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
        return new LoggerConfig(true, "32m", "33m", "31m");
    }

    public static LoggerConfig loadFromResources() {
        try {
            ExcelNode config = ExcelNode.load(Path.of(LoggerConfig.class.getResource("logger.txt").toURI()));

            return new LoggerConfig(Boolean.parseBoolean(config.getRow("Enable").getValue("Value")),
                    config.getRow("Info").getValue("Color"),
                    config.getRow("Warning").getValue("Color"),
                    config.getRow("Error").getValue("Color"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
