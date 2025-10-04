package astrogeist.engine.integration.api.astrometry.model;

import java.util.Set;

import astrogeist.common.Empty;

public final class Tags extends AstrometryModel {

	private final String[] tags;
	
	public Tags(Set<String> tags) { this.tags = tags.toArray(Empty.StringArray); }
	
	@Override public final String toString() {
		var sb = new StringBuilder();
		appendNameValues(Names.TAGS, this.tags, sb);
		return sb.toString();
	}
}
