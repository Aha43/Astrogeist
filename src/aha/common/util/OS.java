package aha.common.util;

/**
 * <p>
 *   Utility methods of use when reasoning about the operating system running 
 *   in. 
 * </p>
 */
public final class OS {
	private OS() { Guards.throwStaticClassInstantiateError(); }
	
	public static final String WINDOWS = "windows";
	public static final String MAC = "mac";
	public static final String LINUX = "linux";
	
	/**
	 * <p>
	 *   Gets best guess of being on
	 *   {@link #WINDOWS},
	 *   {@link #MAC} or
	 *   {@link #LINUX}.
	 * </p>
	 * @return Name on OS likely run on.
	 */
	public final static String osName() {
        String os = System.getProperty("os.name","").toLowerCase();
        if (os.contains("win")) return WINDOWS;
        if (os.contains("mac")) return MAC;
        return LINUX;
    }
	
}
