package astrogeist.engine.integration.api.astrometry.model.builder;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import astrogeist.engine.integration.api.astrometry.model.Annotation;
import astrogeist.engine.integration.api.astrometry.model.Names;

public final class AnnotationBuilder extends AstrometryModelBuilder<Annotation> {
	
	private String type = "";
	private Set<String> names = new LinkedHashSet<>();
	private double pixelx = 0.0d;
	private double pixely = 0.0d;
	private double radius = 0.0d;
	
	public final void clear() {
		this.type = "";
		this.names.clear();
		this.pixelx = 0.0d;
		this.pixely = 0.0d;
		this.radius = 0.0d;
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
		this.withType(node.get(Names.TYPE).asText())
			.withPixelx(node.get(Names.PIXELX).asDouble())
			.withPixely(node.get(Names.PIXELY).asDouble())
			.withRadius(node.get(Names.RADIUS).asDouble());
		for (var n : node.get(Names.NAMES)) this.withName(n.asText());
		return this.build();
	}
	
	public final AnnotationBuilder withType(String type) { this.type = type; return this; }
	public final AnnotationBuilder withName(String name) { this.names.add(name); return this; }
	public final AnnotationBuilder withPixelx(double pixelx) { this.pixelx = pixelx; return this; }
	public final AnnotationBuilder withPixely(double pixely) { this.pixely = pixely; return this; }
	public final AnnotationBuilder withRadius(double radius) { this.radius = radius; return this; }
	
}
