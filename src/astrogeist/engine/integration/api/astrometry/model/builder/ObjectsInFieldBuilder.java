package astrogeist.engine.integration.api.astrometry.model.builder;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import astrogeist.common.Guards;
import astrogeist.engine.integration.api.astrometry.model.Names;
import astrogeist.engine.integration.api.astrometry.model.ObjectsInField;

public final class ObjectsInFieldBuilder extends AstrometryModelBuilder<ObjectsInField> {
	
	private final Set<String> objectsInField = new LinkedHashSet<>();

	@Override public final ObjectsInField build() { return new ObjectsInField(this.objectsInField); }

	@Override public ObjectsInField build(JsonNode node) {
		var objects = node.get(Names.OBJECTS_IN_FIELD);
		for (var n : objects) this.withObjectInField(n.asText());
		return this.build();
	}

	@Override public void clear() { this.objectsInField.clear(); }
	
	public final ObjectsInFieldBuilder withObjectInField(String object) {
		Guards.requireNonEmpty(object, "object");
		this.objectsInField.add(object);
		return this;
	}

}
