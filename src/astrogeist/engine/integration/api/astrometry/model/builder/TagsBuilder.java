package astrogeist.engine.integration.api.astrometry.model.builder;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import astrogeist.common.Common;
import astrogeist.engine.integration.api.astrometry.model.Names;
import astrogeist.engine.integration.api.astrometry.model.Tags;

public final class TagsBuilder extends AstrometryModelBuilder<Tags> {
	
	private final Set<String> tags = new LinkedHashSet<>();

	@Override public final Tags build() { return new Tags(this.tags); }

	@Override public Tags build(JsonNode node) {
		var objects = node.get(Names.TAGS);
		for (var n : objects) this.withTag(n.asText());
		return this.build();
	}

	@Override public void clear() { this.tags.clear(); }
	
	public final TagsBuilder withTag(String tag) {
		Common.requireNonEmpty(tag, "tag");
		this.tags.add(tag);
		return this;
	}

}
