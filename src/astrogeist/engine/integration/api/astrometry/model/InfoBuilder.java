package astrogeist.engine.integration.api.astrometry.model;

import java.util.LinkedHashSet;
import java.util.Set;

import astrogeist.Common;

public final class InfoBuilder {
	private String status = "unknown";
	private String originalFileName = "";
	private final Set<String> objectsInField = new LinkedHashSet<>();
	private final Set<String> machineTags = new LinkedHashSet<>();
	private final Set<String> tags = new LinkedHashSet<>();
	
	private final CalibrationBuilder calibrationBuilder = new CalibrationBuilder();
	
	public InfoBuilder() {}
	
	public InfoBuilder(String status) { withStatus(status); }
	
	public final Info build() {
		return new Info(
			this.status,
			this.originalFileName,
			this.calibrationBuilder.build(),
			new LinkedHashSet<String>(this.objectsInField),
			new LinkedHashSet<String>(this.machineTags),
			new LinkedHashSet<String>(this.tags));
	}
	
	public boolean isSuccessStatus() { return "success".equalsIgnoreCase(this.status); }
	
	public final InfoBuilder withStatus(String status) {
		Common.requireNonEmpty(status, "status");
		this.status = status;
		return this;
	}
	
	public final InfoBuilder withOriginalFileName(String originalFileName) {
		Common.requireNonEmpty(originalFileName, "originalFileName");
		this.originalFileName = originalFileName;
		return this;
	}
	
	public final InfoBuilder withObjectInField(String object) {
		Common.requireNonEmpty(object, "object");
		this.objectsInField.add(object);
		return this;
	}
	
	public final InfoBuilder withMachineTag(String machineTag) {
		Common.requireNonEmpty(machineTag, "machineTag");
		this.machineTags.add(machineTag);
		return this;
	}
	
	public final InfoBuilder withTag(String tag) {
		Common.requireNonEmpty(tag, "tag");
		this.tags.add(tag);
		return this;
	}
	
	public final CalibrationBuilder calibrationBuilder() { return this.calibrationBuilder; }
}
