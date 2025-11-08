package aha.common.io.appdata;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import aha.common.abstraction.appdata.AppData;
import aha.common.abstraction.appdata.AppDataAccessor;
import aha.common.abstraction.appdata.AppDataManager;
import aha.common.abstraction.appdata.AppDataReader;
import aha.common.abstraction.appdata.AppDataWriter;
import aha.common.logging.Log;
import aha.common.util.Cast;

/**
 * <p>
 *   Default 
 *   {@link AppDataManager} implementation.
 * </p>
 */
public final class DefaultAppDataManager implements AppDataManager {
	private final Logger logger = Log.get(this);
	
	private final AppDataAccessor accessor;
	
	private final Map<Class<?>, AppDataReader> readers = new HashMap<>();
	private final Map<Class<?>, AppDataWriter> writers = new HashMap<>();
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param accessor the {@link AppDataAccessor} to read data.
	 * @param ads      the {@link AppDataReader}s and 
	 *                 {@link AppDataWriter}s to use.
	 */
	public DefaultAppDataManager(AppDataAccessor accessor, 
		AppData... ads) {
		
		this.accessor = accessor;
		for (var ad : ads) this.add(ad);
	}
	
	private final void add(AppData ad) {
		var type = ad.type();
		var reader = Cast.as(AppDataReader.class, ad);
		if (reader != null) {
			this.logger.info("add : '" + type.getName() +
				"' as reader of astrogeist data");
			this.readers.put(type, reader);
		}
		var writer = Cast.as(AppDataWriter.class, ad);
		if (writer != null) {
			this.logger.info("add : '" + type.getName() +
				"' as writer of astrogeist data");
			this.writers.put(type, writer);
		}
	}
	
	@Override public final <T> T load(Class<T> type) {
		try {
			Objects.requireNonNull(type, "type");
		
			this.logger.info("load astrogeist data using reader of type : '" +
				type.getName() + "'");
		
			var reader = this.readers.get(type);
			if (reader == null) {
				throw new IllegalArgumentException(
					"No reader found for data type : '" + type + "'");
			}
		
			var retVal = this.accessor.load(reader);
			return Cast.asOrThrow(type, retVal);
		} catch (Exception x) {
			logger.log(Level.SEVERE, "Failed to save settingd", x);
			throw new RuntimeException(x);
		}
	}
	
	@Override public final void save(Object data) throws Exception {
		Objects.requireNonNull(data, "data");
		
		var type = data.getClass();
		
		this.logger.info("save astrogeist data using writer of type : '" +
			type.getName() + "'");
		
		var writer = this.writers.get(type);
		if (writer == null) {
			throw new IllegalArgumentException(
				"No writer found for data type : '" + type + "'");
		}
		
		this.accessor.save(writer, data);
	}
	
}
