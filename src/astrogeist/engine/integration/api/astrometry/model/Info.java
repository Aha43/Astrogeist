package astrogeist.engine.integration.api.astrometry.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public final class Info {
	
	private final String status;
	
	private final String originalFileName;
	
	private final Calibration calibration;
	
	private final Set<String> objectsInField;
	private final Set<String> machineTags;
	private final Set<String> tags;
	
	Info(
		String status,
		String originalFileName,
		Calibration calibration,
		LinkedHashSet<String> objectsInField,
		LinkedHashSet<String> machineTags,
		LinkedHashSet<String> tags) {
		
		this.status = status;
		
		this.originalFileName = originalFileName;
		
		this.calibration = calibration;
		
		this.objectsInField = objectsInField;
		this.machineTags = machineTags;
		this.tags = tags;
	}
	
	public final String status() { return this.status; }
	public final String originalFileName() { return this.originalFileName; }
	public final Calibration calibration() { return this.calibration; }
	public final Set<String> objectsInField(){ return Collections.unmodifiableSet(this.objectsInField); }
	public final Set<String> machineTags(){ return Collections.unmodifiableSet(this.machineTags); }
	public final Set<String> tags(){ return Collections.unmodifiableSet(this.tags); }
	
	@Override public final String toString() {
		var ls = System.lineSeparator();
		
		var strb = new StringBuilder();
		
		strb.append("status='").append(this.status).append("'").append(ls)
			.append("original-file-name='").append(this.originalFileName).append("'").append(ls)
			.append(this.calibration())
			.append("objects-in-field='").append(String.join(",", this.objectsInField)).append("'").append(ls)
			.append("machine-tags='").append(String.join(",", this.machineTags)).append("'").append(ls)
			.append("tags='").append(String.join(",", this.tags)).append("'");
		
		return strb.toString();
	}

}
