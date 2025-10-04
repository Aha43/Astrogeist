package astrogeist.engine.integration.api.astrometry.model.builder;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import astrogeist.engine.integration.api.astrometry.model.Annotation;
import astrogeist.engine.integration.api.astrometry.model.Annotations;
import astrogeist.engine.integration.api.astrometry.model.Names;

public final class AnnotationsBuilder extends AstrometryModelBuilder<Annotations> {
	
	private final List<Annotation> annotations = new ArrayList<>();
	
	public final Annotations build() {
		return new Annotations(this.annotations.toArray(new Annotation[0])); }
	
	public final Annotations build(JsonNode node) {
		var builder = new AnnotationBuilder();
		for (var n : node.get(Names.ANNOTATIONS)) {
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
