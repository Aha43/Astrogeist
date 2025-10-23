package astrogeist.engine.persitence;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import astrogeist.common.Guards;
import astrogeist.engine.abstraction.persistence.AstrogeistDataHandler;
import astrogeist.engine.abstraction.persistence.AstrogeistDataReaderWriter;
import astrogeist.engine.abstraction.persistence.AstrogeistStorage;

public abstract class DefaultAstrogeistStorage implements AstrogeistStorage {
	
	private final Map<String, AstrogeistDataHandler> handlers = new HashMap<>();
	private final Map<String, AstrogeistDataReaderWriter> readWriters = new HashMap<>();
	
	@Override public final void configure(String type, AstrogeistDataHandler handler,
		AstrogeistDataReaderWriter readerWriter) {
		
		Guards.requireNonEmpty(type, "type");
		Objects.requireNonNull(handler, "handler");
		Objects.requireNonNull(readerWriter, "readerWriter");
		
		this.handlers.put(type, handler);
		this.readWriters.put(type, readerWriter);
	}
	
	@Override public final Object load(String type) throws Exception {
		Guards.requireNonEmpty(type, "type");
		
		var handler = this.handlers.get(type);
		var reader = this.readWriters.get(type);
		return handler.load(reader);
	}
	
	@Override public final void save(String type, Object data) throws Exception {
		Guards.requireNonEmpty(type, "type");
		Objects.requireNonNull(data, "data");
		
		var handler = this.handlers.get(type);
		var writer = this.readWriters.get(type);
		handler.save(writer, data);
	}

}
