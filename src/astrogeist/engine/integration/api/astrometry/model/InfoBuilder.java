package astrogeist.engine.integration.api.astrometry.model;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import astrogeist.Common;

public final class InfoBuilder {
	
	public static final String STATUS = "status";
	public static final String ORIGINAL_FILENAME = "original_filename";
	public static final String CALIBRATION = "calibration";
	public static final String OBJECTS_IN_FIELD = "objects_in_field";
	public static final String MACHINE_TAGS = "machine_tags";
	public static final String TAGS = "tags";
	
	private String status = "unknown";
	private String originalFileName = "";
	private final Set<String> objectsInField = new LinkedHashSet<>();
	private final Set<String> machineTags = new LinkedHashSet<>();
	private final Set<String> tags = new LinkedHashSet<>();
	
	public final Info build(Calibration calibration) {
		return new Info(
			this.status,
			this.originalFileName,
			calibration,
			new LinkedHashSet<String>(this.objectsInField),
			new LinkedHashSet<String>(this.machineTags),
			new LinkedHashSet<String>(this.tags));
	}
	
	public final InfoBuilder clear() {
		this.status = "unknown";
		this.originalFileName = "";
		this.objectsInField.clear();
		this.machineTags.clear();
		this.tags.clear();
		return this;
	}
	
	public final Info build(JsonNode node) {
		this.withStatus(node.get(STATUS).asText())
			.withOriginalFileName(node.get(ORIGINAL_FILENAME).asText());
		
		var objects = node.get(OBJECTS_IN_FIELD);
		for (var n : objects) this.withObjectInField(n.asText());
		
		var mtags = node.get(MACHINE_TAGS);
		for (var n : mtags) this.withMachineTag(n.asText());
		
		var tags = node.get(TAGS);
		for (var n : tags) this.withTag(n.asText());
		
		var cal = node.get(CALIBRATION);
		var calibrationBuilder = new CalibrationBuilder();
		var calibration = calibrationBuilder.build(cal);
		
		return this.build(calibration);
	}
	
	public final Info build(String json) throws Exception {
		var mapper = new ObjectMapper();
		var root = mapper.readTree(json);
		return this.build(root);
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
	
}
