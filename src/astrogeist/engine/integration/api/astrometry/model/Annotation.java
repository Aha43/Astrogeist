package astrogeist.engine.integration.api.astrometry.model;

import java.util.Set;

import astrogeist.Empty;

public final class Annotation {
	private final String type;
	private final String[] names;
	private final double pixelx;
	private final double pixely;
	private final double radius;
	
	Annotation(
		String type,
		Set<String> names,
		double pixelx,
		double pixely,
		double radius) {
		
		this.type = type;
		this.names = names.toArray(Empty.StringArray);
		this.pixelx = pixelx;
		this.pixely = pixely;
		this.radius = radius;
	}
	
	public final String type() { return this.type; }
	public final String[] names() { return this.names.clone(); }
	public final double pixelx() { return this.pixelx; }
	public final double pixely() { return this.pixely; }
	public final double radius() { return this.radius; }
}
