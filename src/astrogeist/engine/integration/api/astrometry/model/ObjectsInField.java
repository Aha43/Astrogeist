package astrogeist.engine.integration.api.astrometry.model;

import java.util.Set;

import astrogeist.Empty;

public final class ObjectsInField extends AstrometricModel {

	private final String[] objectsInField;
	
	public ObjectsInField(Set<String> objectsInField) {
		this.objectsInField = objectsInField.toArray(Empty.StringArray); }
	
	@Override public final String toString() {
		var sb = new StringBuilder();
		appendNameValues(Names.OBJECTS_IN_FIELD, this.objectsInField, sb);
		return sb.toString();
	}
}
