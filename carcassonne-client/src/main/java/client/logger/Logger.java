package client.logger;

import client.config.LoggerConfig;
import logic.player.Player;

import static java.lang.System.out;

public class Logger {
    private static final String ANSI_PREFIX = "\u001B[0";
    private static LoggerConfig config = LoggerConfig.getDefaultConfig();

    private Logger() {
        // ignored
    }

    /**
     * Logs a message to the console with the debug color.
     *
     * @param message The message to log.
     * @param args    The arguments to format the message with.
     */
    public static void debug(LoggerCategory category, String message, Object... args) {
        debug(category, String.format(message, args));
    }

    /**
     * Logs a message to the console with the debug color.
     *
     * @param message The message to log.
     */
    public static void debug(LoggerCategory category, String message) {
        print(category, message, config.getDebugColor(), LogLevel.DEBUG);
    }

    /**
     * Logs a message to the console with the info color.
     *
     * @param message The message to log.
     * @param args    The arguments to format the message with.
     */
    public static void info(LoggerCategory category, String message, Object... args) {
        info(category, String.format(message, args));
    }

    /**
     * Logs a message to the console with the info color.
     *
     * @param message The message to log.
     */
    public static void info(LoggerCategory category, String message) {
        print(category, message, config.getInfoColor(), LogLevel.INFO);
    }

    /**
     * Logs a message to the console with the warning color.
     *
     * @param message The message to log.
     * @param args    The arguments to format the message with.
     */
    public static void warn(LoggerCategory category, String message, Object... args) {
        warn(category, String.format(message, args));
    }

    /**
     * Logs a message to the console with the warning color.
     *
     * @param message The message to log.
     */
    public static void warn(LoggerCategory category, String message) {
        print(category, message, config.getWarningColor(), LogLevel.WARN);
    }

    /**
     * Logs a message to the console with the error color.
     *
     * @param message The message to log.
     * @param args    The arguments to format the message with.
     */
    public static void error(LoggerCategory category, String message, Object... args) {
        error(category, String.format(message, args));
    }

    /**
     * Logs a message to the console with the error color.
     *
     * @param message The message to log.
     */
    public static void error(LoggerCategory category, String message) {
        print(category, message, config.getErrorColor(), LogLevel.ERROR);
    }

    /**
     * Logs a message to the console with the player color.
     *
     * @param category Category of the log
     * @param player   Player
     * @param message  The message to log
     */
    public static void player(LoggerCategory category, Player player, String message) {
        print(category, message, config.getPlayerColor(player.getGame().getPlayerIndex(player)), LogLevel.INFO);
    }

    /**
     * Logs a message to the console with the player color.
     *
     * @param category Category of the log
     * @param player   Player
     * @param message  The message to log
     */
    public static void player(LoggerCategory category, Player player, String message, Object... args) {
        print(category, String.format(message, args), config.getPlayerColor(player.getGame().getPlayerIndex(player)), LogLevel.INFO);
    }

    /**
     * Prints a message to the console with the given color.
     *
     * @param message       The message to print.
     * @param ansiColorCode The color to print the message in.
     */
    private static void print(LoggerCategory category, String message, String ansiColorCode, LogLevel level) {
        if (level.ordinal() >= config.getLevel().ordinal()) {
            synchronized (out) {
                out.print("\u001B[");
                out.print(ansiColorCode);
                out.print(category);
                out.print(": ");
                out.print(message);
                out.print(ANSI_PREFIX);
                out.println("0m");
            }
        }
    }

    /**
     * Sets the configuration for the logger.
     *
     * @param config The configuration to use.
     */
    public static void setConfig(LoggerConfig config) {
        Logger.config = config;
    }

    public static void setLevel(LogLevel logLevel) {
        config.setLevel(logLevel);
    }
}
