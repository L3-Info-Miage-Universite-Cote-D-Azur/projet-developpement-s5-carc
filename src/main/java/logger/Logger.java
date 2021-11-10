package logger;

import static java.lang.System.*;

/**
 *  The logger
 */
public final class Logger {
    private static boolean isEnabled;

    /**
     * Enable the logger
     */
    public static void enable() {
        isEnabled = true;
    }

    /**
     * Disable the logger
     */
    public static void disable() {
        isEnabled = false;
    }

    /**
     * Display an information log
     *
     * @param message The message to display
     * @param params All the params
     */
    public static void info(String message, Object... params) {
        if (isEnabled)
            out.println(String.format(message, params));
    }

    /**
     * Display a warning log
     *
     * @param message The message to display
     * @param params All the params
     */
    public static void warning(String message, Object... params) {
        if (isEnabled)
            out.println(String.format(message, params));
    }

    /**
     * Display a error log
     *
     * @param message The message to display
     * @param params All the params
     */
    public static void error(String message, Object... params) {
        if (isEnabled)
            err.println(String.format(message, params));
    }
}
