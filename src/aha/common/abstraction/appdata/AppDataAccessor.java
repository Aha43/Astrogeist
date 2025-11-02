package aha.common.abstraction.appdata;

/**
 * <p>
 *   Interface for objects that know how to access physical application data
 *   (i.e. from file system) and so load / save data using passed
 *   {@link AppDataReader} (load) and
 *   {@link AppDataWriter} (save).
 * </p>
 */
public interface AppDataAccessor {
	
	/**
	 * <p>
	 *   Reads data and uses passed
	 *   {@link AppDataReader} to parse into application data.
	 * </p>
	 * @param reader the parser for the data.
	 * @return the application data read by this and parsed by {@code reader}.
	 * @throws Exception If fails.
	 */
	Object load(AppDataReader reader) throws Exception;
	
	/**
	 * <p>
	 *   Writes application data the passed
	 *   {@link AppDataWriter} serialize.
	 * </p>
	 * @param writer the serializer.
	 * @param data   the application data to save.   
	 * @throws Exception If fails.
	 */
	void save(AppDataWriter writer, Object data) throws Exception;
}
