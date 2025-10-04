package astrogeist.engine.integration.api.astrometry.model.builder;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import astrogeist.common.Common;
import astrogeist.engine.integration.api.astrometry.model.MachineTags;
import astrogeist.engine.integration.api.astrometry.model.Names;

public final class MachineTagsBuilder extends AstrometryModelBuilder<MachineTags> {
	
	private final Set<String> machineTags = new LinkedHashSet<>();

	@Override public final MachineTags build() { return new MachineTags(this.machineTags); }

	@Override public MachineTags build(JsonNode node) {
		var objects = node.get(Names.TAGS);
		for (var n : objects) this.withMachineTag(n.asText());
		return this.build();
	}

	@Override public void clear() { this.machineTags.clear(); }
	
	public final MachineTagsBuilder withMachineTag(String machineTag) {
		Common.requireNonEmpty(machineTag, "machineTag");
		this.machineTags.add(machineTag);
		return this;
	}

}
