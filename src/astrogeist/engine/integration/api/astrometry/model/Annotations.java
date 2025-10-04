package astrogeist.engine.integration.api.astrometry.model;

public final class Annotations extends AstrometryModel {
	private final Annotation[] annotations;
	
	public Annotations(Annotation[] annotations) {
		this.annotations = annotations.clone();
	}
	
	public Annotation[] annotations() { return this.annotations.clone(); }
	
	@Override public final String toString() {
		var ls = System.lineSeparator();
		var sb = new StringBuilder();
		for (var a : this.annotations) {
			sb.append(a.toString()).append(ls);
		}
		return sb.toString();
	}
	
}
