package aha.common.abstraction.appdata;

/**
 * <p>
 *   Interface for objects that organizing loading and saving of application
 *   data orchestrating
 *   {@link AppDataAccessor}s,
 *   {@link AppDataReader}s and
 *   {@link AppDataWriter}s. 
 * </p>
 */
public interface AppDataManager {
	
	/**
	 * <p>
	 *   Loads application data.
	 * </p>
	 * @param <T>  the type application data to load. 
	 * @param type the class of application data to load.
	 * @return the application data.
	 * @throws Exception If fails.
	 */
	<T> T load(Class<T> type) throws Exception;
	
	/**
	 * <p>
	 *   Save application data.
	 * </p>
	 * @param data the application data to save.
	 * @throws Exception If fails.
	 */
	void save(Object data) throws Exception;
}
