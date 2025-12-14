package astrogeist.engine.integration.api.astrometry.model.builder;

import com.fasterxml.jackson.databind.JsonNode;

import aha.common.util.Strings;
import astrogeist.engine.integration.api.astrometry.model.Names;
import astrogeist.engine.integration.api.astrometry.model.Status;

public class StatusBuilder extends AstrometryModelBuilder<Status> {
	private String status = Strings.EMPTY;
	
	@Override public final Status build() { return new Status(this.status); }

	@Override public final Status build(JsonNode node) {
		return this.withStatus(node.get(Names.STATUS).asText()).build(); }

	@Override public final void clear() { this.status = Strings.EMPTY; }
	
	public final StatusBuilder withStatus(String status) { 
		this.status = status; return this; }
}
