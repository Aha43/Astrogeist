package astrogeist.engine.integration.api.astrometry.model.builder;

import static aha.common.guard.StringGuards.requireNonEmpty;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import astrogeist.engine.integration.api.astrometry.model.Names;
import astrogeist.engine.integration.api.astrometry.model.Tags;

public final class TagsBuilder extends AstrometryModelBuilder<Tags> {
	
	private final Set<String> tags = new LinkedHashSet<>();

	@Override public final Tags build() { return new Tags(this.tags); }

	@Override public final Tags build(JsonNode node) {
		var objects = node.get(Names.TAGS);
		for (var n : objects) this.withTag(n.asText());
		return this.build();
	}

	@Override public final void clear() { this.tags.clear(); }
	
	public final TagsBuilder withTag(String tag) {
		this.tags.add(requireNonEmpty(tag, "tag")); return this; }
}
