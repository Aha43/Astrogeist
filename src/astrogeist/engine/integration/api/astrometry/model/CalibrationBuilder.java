package astrogeist.engine.integration.api.astrometry.model;

public final class CalibrationBuilder {
	private double ra = 0.0d;
	private double dec = 0.0d;
	private double radius = 0.0d;
	private double pixscale = 0.0d;
	private double orientation = 0.0d;
	private int parity = 0;
	
	public CalibrationBuilder() {}
	
	public final Calibration build() {
		return new Calibration(this.ra, this.dec, this.radius, 
			this.pixscale, this.orientation, this.parity);
	}
	
	public final double ra() { return this.ra; }
	public final double dec() { return this.dec; }
	public final double radius() { return this.radius; }
	public final double pixscale() { return this.pixscale; }
	public final double orientation() { return this.orientation; }
	public final int parity() { return this.parity; }
	
	public CalibrationBuilder withRa(double ra) { this.ra = ra; return this; }
	public CalibrationBuilder withDec(double dec) { this.dec = dec; return this; }
	public CalibrationBuilder withRadius(double radius) { this.radius = radius; return this; }
	public CalibrationBuilder withPixscale(double pixscale) { this.pixscale = pixscale; return this; }
	public CalibrationBuilder withOrientation(double orientation) { this.orientation = orientation; return this; }
	public CalibrationBuilder withParity(int parity) { this.parity = parity; return this; }
}
