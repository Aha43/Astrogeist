package astrogeist.engine.typesystem;

public enum TypeKeywords {
	FILE,
	BINNING,
	EXPOSURE,
	GAIN,
	FRAMECOUNT,
	CONFIGURATION;
	
	public String word() { return this.name().toLowerCase(); }
}
