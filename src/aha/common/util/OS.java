package aha.common.util;

import static java.lang.System.getProperty;

/**
 * <p>
 *   Enum for OS classes. 
 * </p>
 */
public enum OS {
	
	WINDOWS("windows"),
	MAC("mac"),
	LINUX("linux");
	
	private final String name;
	
	private OS(String name) { this.name = name; }
	
	/**
	 * <p>
	 *   Gets OS name.
	 * </p>
	 * @return Name of OS.
	 */
	public String getName() { return this.name; }
	
	/**
	 * <p>
     *   Attempts to guess the current operating system based on the "os.name" 
     *   system property.
     * </p>
     * <p>
     *   This method retrieves the system property {@code "os.name"} 
     *   and performs a case-insensitive check for common identifiers 
     *   ("win", "mac"). It defaults to {@code LINUX} if neither Windows nor
     *   macOS identifiers are found.
     * </p>
     * @return The determined {@link OS} enum constant (WINDOWS, MAC, or LINUX).
     */
	public static OS guess() {
		var os = getProperty("os.name", Strings.EMPTY).toLowerCase();
		if (os.contains("win")) return WINDOWS;
		if (os.contains("mac")) return MAC;
		return LINUX;
	}
	
}
