package aha.common.abstraction.io.appdata;

import java.io.InputStream;

/**
 * <p>
 *   Interface for objects that parse application data of type
 *   {@link #type() type}.
 * </p>
 */
public interface AppDataReader extends AppData {
	
	/**
	 * <p>
	 *   Parse application data.
	 * </p>
	 * @param in Stream to read from, provided by a 
	 *           {@link AppDataAccessor}.
	 * @return Application data deseralized.
	 * @throws Exception If fails.
	 */
	Object read(InputStream in) throws Exception;
	
	/**
	 * <p>
	 *   Creates the default object of the application data this know.
	 * </p>
	 * <p>
	 *   Used when system is asked for application data of 
	 *   {@link #type() type} for very first time.
	 * </p>
	 * @return Default.
	 */
	Object createDefault();
}
