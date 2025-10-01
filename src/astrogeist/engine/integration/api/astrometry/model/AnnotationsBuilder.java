package astrogeist.engine.integration.api.astrometry.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public final class AnnotationsBuilder extends AstrometricModelBuilder<Annotations> {
	public static final String ANNOTATIONS = "annotations";
	
	private final List<Annotation> annotations = new ArrayList<>();
	
	public final Annotations build() {
		return new Annotations(this.annotations.toArray(new Annotation[0])); }
	
	public final Annotations build(JsonNode node) {
		var builder = new AnnotationBuilder();
		for (var n : node.get(ANNOTATIONS)) {
			var annotation = builder.build(n);
			this.annotations.add(annotation);
			builder.clear();
		}
		
		return this.build();
	}
	
	public final void clear() { this.annotations.clear(); }
	
	public final AnnotationsBuilder withAnnotation(Annotation annotation) {
		this.annotations.add(annotation);
		return this;
	}
	
}
