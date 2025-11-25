package aha.common.logging;

import static aha.common.util.Guards.throwStaticClassInstantiateError;

import java.io.IOException;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * <p>
 *   Logging.
 * </p>
 */
public final class Log {
    private Log() { throwStaticClassInstantiateError(); }

    private static final ConcurrentHashMap<String, Logger> cache =
    	new ConcurrentHashMap<>();

    // Global state
    private static volatile Level currentLevel =
    	Level.WARNING; // default: hide INFO unless debugging
    private static volatile FileHandler fileHandler;

    
    /**
     * <p>
     *   Gets
     *   {@link Logger} to use.
     * </p>
     * @param o the object doing the logging.
     * @return the {@link Logger} to use.
     */
    public final static Logger get(Object o) { return get(o.getClass()); }
    
    /**
     * <p>
     *   Gets
     *   {@link Logger} to use.
     * </p>
     * @param cls the class of the object doing the logging.
     * @return the {@link Logger} to use.
     */
    public final static Logger get(Class<?> cls) {
        String fullName = cls.getName();
        return cache.computeIfAbsent(fullName, Log::createLogger);
    }

    private final static Logger createLogger(String name) {
        Logger logger = Logger.getLogger(name);
        logger.setUseParentHandlers(false);

        ConsoleHandler console = new ConsoleHandler();
        console.setFormatter(new SimpleFormatter());
        console.setLevel(currentLevel);

        logger.addHandler(console);
        if (fileHandler != null) {
            logger.addHandler(fileHandler); }
        logger.setLevel(currentLevel);
        return logger;
    }

    // ----- Global level control -----

    /**
     * <p>
     *   Gets the current global log
     *   {@link Level}.
     * </p>
     * @return the {@link Level}.
     */
    public final static Level getGlobalLevel() { return currentLevel; }

    /**
     * <p>
     *   Sets the current global log
     *   {@link Level}.
     * </p>
     * @param level the {@link Level} to set.
     */
    public final static synchronized void setGlobalLevel(Level level) {
        Level old = currentLevel;
        if (old == level) return;

        Logger markerLog = get("control"); // or "root" if you prefer

        // Pre-change marker: visible at the *current* level
        if (markerLog.isLoggable(Level.INFO)) {
            markerLog.info(marker("Switching log level", old, level));
        } else if (markerLog.isLoggable(Level.WARNING)) {
            markerLog.warning(marker("Switching log level", old, level));
        } else {
            markerLog.severe(marker("Switching log level", old, level));
        }

        // Apply new level to all loggers + handlers
        currentLevel = level;
        for (Logger l : cache.values()) {
            l.setLevel(level);
            for (Handler h : l.getHandlers()) h.setLevel(level);
        }
        if (fileHandler != null) fileHandler.setLevel(level);

        // Post-change marker: guaranteed visible even if you just turned
        // logging down
        if (level.intValue() <= Level.INFO.intValue()) {
            markerLog.info(marker("Log level is now", old, level));
        } else {
            markerLog.warning(marker("Log level is now", old, level));
        }
    }
    
    private static final DateTimeFormatter TS =
    	DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private final static String marker(String prefix, Level from, Level to) {
        String ts = ZonedDateTime.now().format(TS);
        return "════════ " + prefix + " [" + from.getName() + " → " +
        	to.getName() + "] at " + ts + " ════════";
    }

    // ----- Optional file logging -----

    public final static synchronized void enableFileLogging(Path path,
    	boolean append) throws IOException {
        
    	disableFileLogging();
        fileHandler = new FileHandler(path.toString(), append);
        fileHandler.setFormatter(new SimpleFormatter());
        fileHandler.setLevel(currentLevel);

        // attach to existing loggers
        for (Logger l : cache.values()) l.addHandler(fileHandler);
    }

    public final static synchronized void disableFileLogging() {
        if (fileHandler != null) {
            for (Logger l : cache.values()) l.removeHandler(fileHandler);
            try { fileHandler.close(); } catch (Exception ignored) {}
            fileHandler = null;
        }
    }

    public static boolean isFileLoggingEnabled() { return fileHandler != null; }

    // Root convenience
    private static final Logger root = get("root");
    public final static void info(String msg)  { root.info(msg); }
    public final static void warn(String msg)  { root.warning(msg); }
    public final static void error(String msg) { root.severe(msg); }
    public final static void debug(String msg) { root.fine(msg); }
    public final static void error(String msg, Throwable t) { 
    	root.log(Level.SEVERE, msg, t); }
    public final static void debug(String msg, Throwable t) { 
    	root.log(Level.FINE, msg, t); }
    
    // Error convenience
    public final static void error(Logger logger, Throwable t) {
    	error(logger, "Severe error", t); }
    public final static void error(Logger logger, String msg, Throwable t) {
    	logger.log(Level.SEVERE, msg, t); }
    
    private static boolean recordStories = false;
    
    public static void recordStories(boolean v) { recordStories = v; }
    public static boolean recordStories() { return recordStories; }
}
