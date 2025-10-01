package astrogeist.engine.integration.api.astrometry.model;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

public final class AnnotationBuilder extends AstrometricModelBuilder<Annotation> {
	public final String TYPE = "type";
	public final String NAMES = "names";
	public final String PIXELX = "pixelx";
	public final String PIXELY = "pixely";
	public final String RADIUS = "radius"; 
	
	private String type = "";
	private Set<String> names = new LinkedHashSet<>();
	private double pixelx = 0.0d;
	private double pixely = 0.0d;
	private double radius = 0.0d;
	
	public final AnnotationBuilder clear() {
		this.type = "";
		this.names.clear();
		this.pixelx = 0.0d;
		this.pixely = 0.0d;
		this.radius = 0.0d;
		return this;
	}
	
	public final Annotation build() {
		return new Annotation(
			this.type,
			this.names,
			this.pixelx,
			this.pixely,
			this.radius);
	}
	
	public final Annotation build(JsonNode node) {
		this.withType(node.get(TYPE).asText())
			.withPixelx(node.get(PIXELX).asDouble())
			.withPixely(node.get(PIXELY).asDouble())
			.withRadius(node.get(RADIUS).asDouble());
		for (var n : node.get(NAMES)) this.withName(n.asText());
		return this.build();
	}
	
	public final AnnotationBuilder withType(String type) { this.type = type; return this; }
	public final AnnotationBuilder withName(String name) { this.names.add(name); return this; }
	public final AnnotationBuilder withPixelx(double pixelx) { this.pixelx = pixelx; return this; }
	public final AnnotationBuilder withPixely(double pixely) { this.pixely = pixely; return this; }
	public final AnnotationBuilder withRadius(double radius) { this.radius = radius; return this; }
	
}
