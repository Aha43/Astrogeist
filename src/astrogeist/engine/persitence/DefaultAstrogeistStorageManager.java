package astrogeist.engine.persitence;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import astrogeist.common.Cast;
import astrogeist.engine.abstraction.persistence.AstrogeistAccessor;
import astrogeist.engine.abstraction.persistence.AstrogeistData;
import astrogeist.engine.abstraction.persistence.AstrogeistDataReader;
import astrogeist.engine.abstraction.persistence.AstrogeistDataWriter;
import astrogeist.engine.abstraction.persistence.AstrogeistStorageManager;
import astrogeist.engine.logging.Log;

public final class DefaultAstrogeistStorageManager implements AstrogeistStorageManager {
	private final Logger logger = Log.get(this);
	
	private final AstrogeistAccessor accessor;
	
	private final Map<Class<?>, AstrogeistDataReader> readers = new HashMap<>();
	private final Map<Class<?>, AstrogeistDataWriter> writers = new HashMap<>();
	
	public DefaultAstrogeistStorageManager(AstrogeistAccessor accessor, AstrogeistData... ads) {
		this.accessor = accessor;
		for (var ad : ads) this.add(ad);
	}
	
	private final void add(AstrogeistData ad) {
		var type = ad.type();
		var reader = Cast.as(AstrogeistDataReader.class, ad);
		if (reader != null) {
			this.logger.info("add : '" + type.getName() + "' as reader of astrogeist data");
			this.readers.put(type, reader);
		}
		var writer = Cast.as(AstrogeistDataWriter.class, ad);
		if (writer != null) {
			this.logger.info("add : '" + type.getName() + "' as writer of astrogeist data");
			this.writers.put(type, writer);
		}
	}
	
	@Override public final <T> T load(Class<T> type) throws Exception {
		Objects.requireNonNull(type, "type");
		
		this.logger.info("load astrogeist data using reader of type : '" + type.getName() + "'");
		
		var reader = this.readers.get(type);
		if (reader == null) {
			throw new IllegalArgumentException("No reader found for data type : '" + type + "'");
		}
		
		var retVal = this.accessor.load(reader);
		return Cast.asOrThrow(type, retVal);
	}
	
	@Override public final <T> void save(Class<T> type, T data) throws Exception {
		Objects.requireNonNull(type, "type");
		Objects.requireNonNull(data, "data");
		
		this.logger.info("save astrogeist data using writer of type : '" + type.getName() + "'");
		
		var writer = this.writers.get(type);
		if (writer == null) {
			throw new IllegalArgumentException("No writer found for data type : '" + type + "'");
		}
		
		this.accessor.save(writer, data);
	}
	
}
