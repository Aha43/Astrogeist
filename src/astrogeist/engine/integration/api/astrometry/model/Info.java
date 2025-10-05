package astrogeist.engine.integration.api.astrometry.model;

import java.util.Set;

import astrogeist.common.Empty;

public final class Info extends AstrometryModel {
	
	private final String status;
	
	private final String originalFileName;
	
	private final Calibration calibration;
	
	private final String[] objectsInField;
	private final String[] machineTags;
	private final String[] tags;
	
	public Info(
		String status,
		String originalFileName,
		Calibration calibration,
		Set<String> objectsInField,
		Set<String> machineTags,
		Set<String> tags) {
		
		this.status = status;
		
		this.originalFileName = originalFileName;
		
		this.calibration = calibration;
		
		this.objectsInField = objectsInField.toArray(Empty.StringArray);
		this.machineTags = machineTags.toArray(Empty.StringArray);
		this.tags = tags.toArray(Empty.StringArray);
	}
	
	public final String status() { return this.status; }
	public final String originalFileName() { return this.originalFileName; }
	public final Calibration calibration() { return this.calibration; }
	public final String[] objectsInField() { return this.objectsInField.clone(); }
	public final String[] machineTags() { return this.machineTags.clone(); }
	public final String[] tags() { return this.tags.clone(); }
	
	@Override public final String toString() {
		var sb = new StringBuilder();
		appendNameValue(Names.STATUS, this.status, sb);
		appendNameValue(Names.ORIGINAL_FILENAME, this.originalFileName, sb);
		sb.append(this.calibration);
		appendNameValues(Names.OBJECTS_IN_FIELD, this.objectsInField, sb);
		appendNameValues(Names.MACHINE_TAGS, this.machineTags, sb);
		appendNameValues(Names.TAGS, this.tags, sb);
		return sb.toString();
	}

}
