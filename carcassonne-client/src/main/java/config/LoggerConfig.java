package config;

import excel.ExcelNode;

import java.nio.file.Path;
import java.nio.file.Paths;

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
        ExcelNode config = ExcelNode.load(Paths.get(LoggerConfig.class.getResource(".").getPath(), "logger.txt"));

        return new LoggerConfig(Boolean.parseBoolean(config.getRow("Enable").getValue("Value")),
                config.getRow("Info").getValue("Color"),
                config.getRow("Warning").getValue("Color"),
                config.getRow("Error").getValue("Color"));
    }
}
