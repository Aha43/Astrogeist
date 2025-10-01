package astrogeist.engine.integration.api.astrometry.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AstrometricModelBuilder<T extends AstrometricModel> {
	public abstract T build();
	public abstract T build(JsonNode node);
	public abstract void clear();
	
	public final T build(String json) throws Exception {
		var mapper = new ObjectMapper();
		var root = mapper.readTree(json);
		return this.build(root);
	}
}
