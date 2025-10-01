package astrogeist.engine.integration.api.astrometry.model;

public final class Calibration extends AstrometricModel {
	private final double ra;
	private final double dec;
	private final double radius;
	private final double pixscale;
	private final double orientation;
	private final int parity;
	
	Calibration(
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
		var ls = System.lineSeparator();
		var strb = new StringBuilder();
		strb.append("ra='").append(this.ra).append("'").append(ls)
			.append("dec='").append(this.dec).append("'").append(ls)
			.append("radius='").append(this.radius).append("'").append(ls)
			.append("pixscale='").append(this.pixscale).append("'").append(ls)
			.append("orientation='").append(this.orientation).append("'").append(ls)
			.append("parity='").append(this.parity).append("'").append(ls);
		var retVal = strb.toString();
		return retVal;
	}
	
}
