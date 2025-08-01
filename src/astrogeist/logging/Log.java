package astrogeist.logging;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import astrogeist.Common;

public final class Log {
    private static final String ROOT_NAME = "astrogeist";
    private static final ConcurrentHashMap<String, Logger> cache = new ConcurrentHashMap<>();

    private Log() { Common.throwStaticClassInstantiateError(); }

    public static Logger get(Object o) { return get(o.getClass()); }

    public static Logger get(Class<?> cls) {
        String fullName = cls.getName();
        String loggerName = fullName.startsWith(ROOT_NAME + ".") ? fullName : ROOT_NAME + "." + fullName;
        return cache.computeIfAbsent(loggerName, Log::createLogger);
    }

    public static Logger get(String moduleSuffix) {
        String loggerName = ROOT_NAME + "." + moduleSuffix;
        return cache.computeIfAbsent(loggerName, Log::createLogger);
    }

    private static Logger createLogger(String name) {
        Logger logger = Logger.getLogger(name);
        logger.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter()); // You can replace with custom formatter
        handler.setLevel(Level.ALL);

        logger.addHandler(handler);
        logger.setLevel(Level.ALL);

        return logger;
    }

    // Root logger for quick use
    private static final Logger root = get("root");
    public static void info(String msg) { root.info(msg); }
    public static void warn(String msg) { root.warning(msg); }
    public static void error(String msg) { root.severe(msg); }
    public static void debug(String msg) { root.fine(msg); }
    public static void error(String msg, Throwable t) { root.log(Level.SEVERE, msg, t); }
    public static void debug(String msg, Throwable t) { root.log(Level.FINE, msg, t); }
}
