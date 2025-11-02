package aha.common.abstraction.appdata;

/**
 * <p>
 *   Interface for objects that understand the structure of application data 
 *   (i.e. parsers and serializers).
 * </p>
 */
public interface AppData {
	
	/**
	 * <p>
	 *   Gets the type of the application data.
	 * </p>
	 * @return Type.
	 */
	Class<?> type();
	
	/**
	 * <p>
	 *   Gets the format of the a application data (i.e. xml, json).
	 * </p>
	 * @return Format.
	 */
	String format();
}
