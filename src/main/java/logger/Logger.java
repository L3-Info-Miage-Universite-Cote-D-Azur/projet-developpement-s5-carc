package logger;

public class Logger {
    public static void info(String message, Object... params) {
        System.out.println(String.format(message, params));
    }

    public static void warning(String message, Object... params) {
        System.out.println(String.format(message, params));
    }

    public static void error(String message, Object... params) {
        System.err.println(String.format(message, params));
    }
}
