package astrogeist.engine.integration.api.astrometry.model.builder;

import com.fasterxml.jackson.databind.JsonNode;

import astrogeist.engine.integration.api.astrometry.model.Names;
import astrogeist.engine.integration.api.astrometry.model.Status;

public class StatusBuilder extends AstrometryModelBuilder<Status> {

	private String status = "";
	
	@Override public Status build() { return new Status(this.status); }

	@Override public Status build(JsonNode node) {
		return this.withStatus(node.get(Names.STATUS).asText()).build(); }

	@Override public void clear() { this.status = ""; }
	
	public final StatusBuilder withStatus(String status) {
		this.status = status;
		return this;
	}

}
