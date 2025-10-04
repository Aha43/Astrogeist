package astrogeist.engine.integration.api.astrometry.model;

public final class Calibration extends AstrometryModel {
	private final double ra;
	private final double dec;
	private final double radius;
	private final double pixscale;
	private final double orientation;
	private final int parity;
	
	public Calibration(
		double ra,
		double dec,
		double radius,
		double pixscale,
		double orientation,
		int parity) {
		
		this.ra = ra;
		this.dec = dec;
		this.radius = radius;
		this.pixscale = pixscale;
		this.orientation = orientation;
		this.parity = parity;
	}
	
	public final double ra() { return this.ra; }
	public final double dec() { return this.dec; }
	public final double radius() { return this.radius; }
	public final double pixscale() { return this.pixscale; }
	public final double orientation() { return this.orientation; }
	public final int parity() { return this.parity; }
	
	@Override public final String toString() {
		var sb = new StringBuilder();
		appendNameValue(Names.RA, this.ra, sb);
		appendNameValue(Names.DEC, this.dec, sb);
		appendNameValue(Names.RADIUS, this.radius, sb);
		appendNameValue(Names.PIXSCALE, this.pixscale, sb);
		appendNameValue(Names.ORIENTATION, this.orientation, sb);
		appendNameValue(Names.PARITY, this.parity, sb);
		return sb.toString();
	}
	
}
