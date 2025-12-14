package astrogeist.engine.integration.api.astrometry.model.builder;

import static aha.common.guard.StringGuards.requireNonEmpty;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import astrogeist.engine.integration.api.astrometry.model.MachineTags;
import astrogeist.engine.integration.api.astrometry.model.Names;

public final class MachineTagsBuilder
	extends AstrometryModelBuilder<MachineTags> {
	
	private final Set<String> machineTags = new LinkedHashSet<>();

	@Override public final MachineTags build() { 
		return new MachineTags(this.machineTags); }

	@Override public final MachineTags build(JsonNode node) {
		var objects = node.get(Names.TAGS);
		for (var n : objects) this.withMachineTag(n.asText());
		return this.build();
	}

	@Override public final void clear() { this.machineTags.clear(); }
	
	public final MachineTagsBuilder withMachineTag(String machineTag) {
		this.machineTags.add(requireNonEmpty(machineTag, "machineTag"));
		return this;
	}
}
