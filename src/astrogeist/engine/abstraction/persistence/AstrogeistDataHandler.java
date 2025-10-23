package astrogeist.engine.abstraction.persistence;

/**
 * <p>
 *   Interface for some data Astrogeist need to persist.
 * </p>
 */
public interface AstrogeistDataHandler {
	
	/**
	 * <p>
	 *   Gets the unique key that identifies this data object.
	 * </p>
	 * @return the key.
	 */
	String key();
	
	/**
	 * <p>
	 *   Loads the data.
	 * </p>
	 * @param <T>     the type of data.
	 * @param  reader the object that know how to de-serialize objects of type {@code T}. 
	 * @return the data. Should never be {@code null}: If data never saved returns the default data.
	 * @throws Exception if fails.
	 */
	Object load(AstrogeistDataReaderWriter reader) throws Exception;
	
	/**
	 * <p>
	 *   Saves the data.
	 * </p>
	 * @param <T>    the type of data.
	 * @param writer the object that know how to serialize objects of type {@code T}.
	 * @param data   the data.
	 * @throws NullPointerException if {@code data == null}.
	 * @throws Exception if fails.
	 */
	void save(AstrogeistDataReaderWriter write, Object data) throws Exception;
}
