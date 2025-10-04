package astrogeist.engine.integration.api.astrometry.model.builder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import astrogeist.engine.integration.api.astrometry.model.AstrometryModel;

public abstract class AstrometryModelBuilder<T extends AstrometryModel> {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	public abstract T build();
	public abstract T build(JsonNode node);
	public abstract void clear();
	
	public final T build(String json) throws Exception {
		var root = MAPPER.readTree(json);
		return this.build(root);
	}
}
