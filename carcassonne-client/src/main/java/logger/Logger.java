package logger;

import config.LoggerConfig;

public class Logger {
    private static LoggerConfig config = LoggerConfig.getDefaultConfig();
    private static String ANSI_PREFIX = "\u001B[0";

    public static void info(String message, Object... args) {
        info(String.format(message, args));
    }

    public static void info(String message) {
        print(message, config.getInfoColor());
    }

    public static void warn(String message, Object... args) {
        warn(String.format(message, args));
    }

    public static void warn(String message) {
        print(message, config.getWarningColor());
    }

    public static void error(String message, Object... args) {
        error(String.format(message, args));
    }

    public static void error(String message) {
        print(message, config.getErrorColor());
    }

    private static void print(String message, String ansiColorCode) {
        if (config.isEnabled()) {
            System.out.print("\u001B[");
            System.out.print(ansiColorCode);
            System.out.print(message);
            System.out.print(ANSI_PREFIX);
            System.out.println("0m");
        }
    }

    public static void setConfig(LoggerConfig config) {
        Logger.config = config;
    }
}
