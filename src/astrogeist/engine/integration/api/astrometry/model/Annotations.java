package astrogeist.engine.integration.api.astrometry.model;

public final class Annotations {
	private final Annotation[] annotations;
	
	public Annotations(Annotation[] annotations) {
		this.annotations = annotations.clone();
	}
	
	public Annotation[] annotations() { return this.annotations.clone(); }
}
