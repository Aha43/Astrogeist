package astrogeist.engine.integration.api.astrometry.model.builder;

import com.fasterxml.jackson.databind.JsonNode;

import astrogeist.engine.integration.api.astrometry.model.Calibration;
import astrogeist.engine.integration.api.astrometry.model.Names;

public final class CalibrationBuilder extends AstrometryModelBuilder<Calibration> {
	
	private double ra = 0.0d;
	private double dec = 0.0d;
	private double radius = 0.0d;
	private double pixscale = 0.0d;
	private double orientation = 0.0d;
	private int parity = 0;
	
	public final void clear() {
		this.ra = 0.0d;
		this.dec = 0.0d;
		this.radius = 0.0d;
		this.pixscale = 0.0d;
		this.orientation = 0.0d;
		this.parity = 0;
	}
	
	public final Calibration build() {
		return new Calibration(this.ra, this.dec, this.radius, 
			this.pixscale, this.orientation, this.parity);
	}
	
	public final Calibration build(JsonNode node) {
		return this
			.withRa(node.get(Names.RA).asDouble())
			.withDec(node.get(Names.DEC).asDouble())
			.withRadius(node.get(Names.RADIUS).asDouble())
			.withPixscale(node.get(Names.PIXSCALE).asDouble())
			.withOrientation(node.get(Names.ORIENTATION).asDouble())
			.withParity(node.get(Names.PARITY).asInt())
			.build();
	}
	
	public CalibrationBuilder withRa(double ra) { this.ra = ra; return this; }
	public CalibrationBuilder withDec(double dec) { this.dec = dec; return this; }
	public CalibrationBuilder withRadius(double radius) { this.radius = radius; return this; }
	public CalibrationBuilder withPixscale(double pixscale) { this.pixscale = pixscale; return this; }
	public CalibrationBuilder withOrientation(double orientation) { this.orientation = orientation; return this; }
	public CalibrationBuilder withParity(int parity) { this.parity = parity; return this; }
}
