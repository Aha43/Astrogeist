package astrogeist.engine.integration.api.astrometry.model;

import com.fasterxml.jackson.databind.JsonNode;

public final class CalibrationBuilder extends AstrometricModelBuilder<Calibration> {
	public static final String RA = "ra";
	public static final String DEC = "dec";
	public static final String RADIUS = "radius";
	public static final String PIXSCALE = "pixscale";
	public static final String ORIENTATION = "orientation";
	public static final String PARITY = "parity";
	
	private double ra = 0.0d;
	private double dec = 0.0d;
	private double radius = 0.0d;
	private double pixscale = 0.0d;
	private double orientation = 0.0d;
	private int parity = 0;
	
	public final CalibrationBuilder clear() {
		this.ra = 0.0d;
		this.dec = 0.0d;
		this.radius = 0.0d;
		this.pixscale = 0.0d;
		this.orientation = 0.0d;
		this.parity = 0;
		return this;
	}
	
	public final Calibration build() {
		return new Calibration(this.ra, this.dec, this.radius, 
			this.pixscale, this.orientation, this.parity);
	}
	
	public final Calibration build(JsonNode node) {
		return this
			.withRa(node.get(RA).asDouble())
			.withDec(node.get(DEC).asDouble())
			.withRadius(node.get(RADIUS).asDouble())
			.withPixscale(node.get(PIXSCALE).asDouble())
			.withOrientation(node.get(ORIENTATION).asDouble())
			.withParity(node.get(PARITY).asInt())
			.build();
	}
	
	public CalibrationBuilder withRa(double ra) { this.ra = ra; return this; }
	public CalibrationBuilder withDec(double dec) { this.dec = dec; return this; }
	public CalibrationBuilder withRadius(double radius) { this.radius = radius; return this; }
	public CalibrationBuilder withPixscale(double pixscale) { this.pixscale = pixscale; return this; }
	public CalibrationBuilder withOrientation(double orientation) { this.orientation = orientation; return this; }
	public CalibrationBuilder withParity(int parity) { this.parity = parity; return this; }
}
