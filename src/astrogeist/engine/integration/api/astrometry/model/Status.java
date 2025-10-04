package astrogeist.engine.integration.api.astrometry.model;

public final class Status extends AstrometryModel {
	
	private final String status;
	
	public Status(String status) { this.status = status; }
	
	public final String status() { return this.status; }
	
	@Override public final String toString() {
		var sb = new StringBuilder();
		appendNameValue(Names.STATUS, this.status, sb);
		return sb.toString();
	}
	
}
