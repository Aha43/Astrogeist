package aha.common.util;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;
import static java.lang.Runtime.getRuntime;
import static java.lang.String.format;

/**
 * <p>
 *   Utility methods of use when keeping an eye on memory (usage).
 * </p>
 */
public final class MemoryUtil {
	private MemoryUtil() { throwStaticClassInstantiateError(); }
	
	/** Bytes in a mega byte. */
	public static final long MB = 1024L * 1024L;
	
	/** Used heap in bytes. */
    public static long usedHeapBytes() {
        var rt = getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }

    /** Max heap (from -Xmx) in bytes. */
    public static long maxHeapBytes() { return getRuntime().maxMemory(); }

    /** Used heap in MB, as double. */
    public static double usedHeapMB() { return bytesToMB(usedHeapBytes()); }

    /** Max heap in MB, as double. */
    public static double maxHeapMB() { return bytesToMB(maxHeapBytes()); }

    /** Bytes to mega bytes. */
    public static double bytesToMB(long bytes) { return bytes / (double) MB; }

    /** Formats mega byte value. */
    public static String formatMB(double mb) { return format("%.1f MB", mb); }

    /** Gets short summary of memory usage. */
    public static String shortSummary() {
        return format(
            "Heap: %s / %s", formatMB(usedHeapMB()), formatMB(maxHeapMB())); }
	
}
