package aha.common.io.appdata;

import static aha.common.util.Guards.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import aha.common.abstraction.io.appdata.AppData;
import aha.common.abstraction.io.appdata.AppDataReader;
import aha.common.abstraction.io.appdata.AppDataWriter;

/**
 * <p>
 *   Base class for 
 *   {@link AppDataReader} and
 *   {@link AppDataWriter}.
 * </p>
 */
public abstract class AbstractAppData implements AppData  {
	private final Class<?> type;
	private final String format;
	
	@Override public final Class<?> type() { return this.type; }
	@Override public final String format() { return this.format; }
	
	/**
	 * <p>
	 *   Creates
	 *   {@link AppData} which
	 *   {@link #format format} is 'xml'.
	 * </p>
	 * @param type Type of object read.
	 */
	protected AbstractAppData(Class<?> type) { this("xml", type); }
	
	/**
	 * <p>
	 *   General constructor.
	 * </p>
	 * @param format Data format.
	 * @param type Type of object read or written.
	 */
	protected AbstractAppData(String format, Class<?> type) { 
		this.format = requireNonEmpty(format, "format");
		this.type = requireNonNull(type, "type");
	}

}
