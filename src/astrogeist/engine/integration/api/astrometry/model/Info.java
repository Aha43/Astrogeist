package astrogeist.engine.integration.api.astrometry.model;

import java.util.Set;

import astrogeist.Empty;

public final class Info extends AstrometricModel {
	
	private final String status;
	
	private final String originalFileName;
	
	private final Calibration calibration;
	
	private final String[] objectsInField;
	private final String[] machineTags;
	private final String[] tags;
	
	Info(
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
	
	@Override public final String toString() {
		var ls = System.lineSeparator();
		
		var strb = new StringBuilder();
		
		strb.append("status='").append(this.status).append("'").append(ls)
			.append("original-file-name='").append(this.originalFileName).append("'").append(ls)
			.append(this.calibration)
			.append("objects-in-field='").append(String.join(",", this.objectsInField)).append("'").append(ls)
			.append("machine-tags='").append(String.join(",", this.machineTags)).append("'").append(ls)
			.append("tags='").append(String.join(",", this.tags)).append("'");
		
		return strb.toString();
	}

}
