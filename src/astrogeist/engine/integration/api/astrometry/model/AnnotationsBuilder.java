package astrogeist.engine.integration.api.astrometry.model;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

public final class AnnotationsBuilder extends AstrometricModelBuilder<Annotations> {
	public static final String ANNOTATIONS = "annotations";
	
	public final Annotations build(JsonNode node) {
		var list = new ArrayList<Annotation>();
		var builder = new AnnotationBuilder();
		for (var n : node.get(ANNOTATIONS)) {
			var annotation = builder.build(n);
			list.add(annotation);
			builder.clear();
		}
		
		return new Annotations(list.toArray(new Annotation[0]));
	}
	
}
