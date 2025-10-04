package astrogeist.engine.logging;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import astrogeist.common.Guards;

public final class LoggingController {
	private LoggingController() { Guards.throwStaticClassInstantiateError(); }
	
    private static final Logger root = Logger.getLogger("");
    private static final Deque<Map<String, Level>> levelStack = new ArrayDeque<>();

    /**
     * <p> 
     *   Apply levels for specific package prefixes. Example: {"astrogeist.engine": FINE, "astrogeist.ui": INFO} 
     * </p>
     */
    public static void apply(Map<String, Level> packageLevels) {
        Objects.requireNonNull(packageLevels, "packageLevels");
        // Ensure console handler prints at least as verbose as the most verbose package we ask for.
        ensureConsoleHandler();

        // Remember current levels for these packages so we can restore.
        Map<String, Level> previous = new LinkedHashMap<>();
        for (String pkg : packageLevels.keySet()) {
            Logger logger = Logger.getLogger(pkg);
            previous.put(pkg, logger.getLevel());
        }
        levelStack.push(previous);

        // Set requested levels.
        for (Map.Entry<String, Level> e : packageLevels.entrySet()) {
            Logger logger = Logger.getLogger(e.getKey());
            logger.setUseParentHandlers(true); // inherit handlers
            logger.setLevel(e.getValue());
        }
    }

    /** Restore the most recent apply() call’s levels. Safe if stack is empty. */
    public static void pop() {
        if (levelStack.isEmpty()) return;
        Map<String, Level> prev = levelStack.pop();
        for (Map.Entry<String, Level> e : prev.entrySet()) {
            Logger logger = Logger.getLogger(e.getKey());
            logger.setLevel(e.getValue()); // may be null (inherit), which is fine
        }
    }

    /** Convenience: parse "astrogeist.engine=FINE, astrogeist.ui=INFO, *=WARNING" */
    public static Map<String, Level> parse(String spec) {
        Map<String, Level> result = new LinkedHashMap<>();
        if (spec == null || spec.isBlank()) return result;
        for (String part : spec.split(",")) {
            String p = part.trim();
            if (p.isEmpty()) continue;
            int i = p.indexOf('=');
            if (i <= 0 || i == p.length() - 1) continue;
            String key = p.substring(0, i).trim();
            String val = p.substring(i + 1).trim().toUpperCase(Locale.ROOT);
            Level level = parseLevel(val);
            if ("*".equals(key)) {
                // Treat * as the root logger
                result.put("", level);
            } else {
                result.put(key, level);
            }
        }
        return result;
    }

    private static Level parseLevel(String s) {
        // Accept JUL names and numbers; fall back to INFO
        try {
            return Level.parse(s);
        } catch (IllegalArgumentException ex) {
            return Level.INFO;
        }
    }

    private static void ensureConsoleHandler() {
        // Add a single ConsoleHandler if not present; keep it simple.
        boolean hasConsole = false;
        for (Handler h : root.getHandlers()) {
            if (h instanceof ConsoleHandler) { hasConsole = true; break; }
        }
        if (!hasConsole) {
            ConsoleHandler ch = new ConsoleHandler();
            ch.setLevel(Level.ALL);
            ch.setFormatter(new SimpleFormatter() {
                private static final String fmt = "%1$tF %1$tT %4$s %2$s - %5$s%6$s%n";
                @Override public synchronized String format(LogRecord r) {
                    String thrown = (r.getThrown() == null) ? "" : ("\n" + stackTrace(r.getThrown()));
                    return String.format(Locale.ROOT, fmt,
                            new Date(r.getMillis()), r.getLoggerName(), r.getSourceMethodName(),
                            r.getLevel().getName(), r.getMessage(), thrown);
                }
            });
            root.addHandler(ch);
            // Let root inherit platform default level; individual packages will override.
        }
    }

    private static String stackTrace(Throwable t) {
        var sw = new java.io.StringWriter();
        t.printStackTrace(new java.io.PrintWriter(sw));
        return sw.toString();
    }
}
