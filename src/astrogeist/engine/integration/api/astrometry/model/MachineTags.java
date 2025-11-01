package astrogeist.engine.integration.api.astrometry.model;

import java.util.Set;

import aha.common.Empty;

public final class MachineTags extends AstrometryModel {

	private final String[] machineTags;
	
	public MachineTags(Set<String> machineTags) {
		this.machineTags = machineTags.toArray(Empty.StringArray); }
	
	public final String[] tags() { return this.machineTags.clone(); }
	
	@Override public final String toString() {
		var sb = new StringBuilder();
		appendNameValues(Names.MACHINE_TAGS, this.machineTags, sb);
		return sb.toString();
	}
}
