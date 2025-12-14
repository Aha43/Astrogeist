package astrogeist.engine.integration.api.astrometry.model.builder;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import astrogeist.engine.integration.api.astrometry.model.Calibration;
import astrogeist.engine.integration.api.astrometry.model.Info;
import astrogeist.engine.integration.api.astrometry.model.Names;

public final class InfoBuilder extends AstrometryModelBuilder<Info> {
	
	private String status = "unknown";
	private String originalFileName = "";
	private final Set<String> objectsInField = new LinkedHashSet<>();
	private final Set<String> machineTags = new LinkedHashSet<>();
	private final Set<String> tags = new LinkedHashSet<>();
	private Calibration calibration = null;
	
	public final Info build() {
		return new Info(
			this.status,
			this.originalFileName,
			this.calibration,
			new LinkedHashSet<String>(this.objectsInField),
			new LinkedHashSet<String>(this.machineTags),
			new LinkedHashSet<String>(this.tags));
	}
	
	public final void clear() {
		this.status = "unknown";
		this.originalFileName = "";
		this.objectsInField.clear();
		this.machineTags.clear();
		this.tags.clear();
		this.calibration = null;
	}
	
	public final Info build(JsonNode node) {
		this.withStatus(node.get(Names.STATUS).asText())
			.withOriginalFileName(node.get(Names.ORIGINAL_FILENAME).asText());
		
		var objects = node.get(Names.OBJECTS_IN_FIELD);
		for (var n : objects) this.withObjectInField(n.asText());
		
		var mtags = node.get(Names.MACHINE_TAGS);
		for (var n : mtags) this.withMachineTag(n.asText());
		
		var tags = node.get(Names.TAGS);
		for (var n : tags) this.withTag(n.asText());
		
		var cal = node.get(Names.CALIBRATION);
		var calibrationBuilder = new CalibrationBuilder();
		var calibration = calibrationBuilder.build(cal);
		this.withCalibration(calibration);
		
		return this.build();
	}
	
	public final boolean isSuccessStatus() { 
		return "success".equalsIgnoreCase(this.status); }
	
	public final InfoBuilder withStatus(String status) {
		this.status = requireNonEmpty(status, "status");
		return this;
	}
	
	public final InfoBuilder withOriginalFileName(String originalFileName) {
		this.originalFileName = requireNonEmpty(originalFileName,
			"originalFileName");
		return this;
	}
	
	public final InfoBuilder withObjectInField(String object) {
		this.objectsInField.add(requireNonEmpty(object, "object")); 
		return this;
	}
	
	public final InfoBuilder withMachineTag(String machineTag) {
		this.machineTags.add(requireNonEmpty(machineTag, "machineTag"));
		return this;
	}
	
	public final InfoBuilder withTag(String tag) {
		this.tags.add(requireNonEmpty(tag, "tag"));
		return this;
	}
	
	public final InfoBuilder withCalibration(Calibration calibration) {
		this.calibration = requireNonNull(calibration, "calibration");
		return this;
	}
}
