package astrogeist.engine.persitence.settings;

import java.util.LinkedHashMap;

public final class Settings {
	private final LinkedHashMap<String, String> data;
	
	public Settings() { this.data = new LinkedHashMap<String, String>(); }
	
	public Settings(LinkedHashMap<String, String> data) { this.data = data; }
	
	public LinkedHashMap<String, String> settings(){ return new LinkedHashMap<String, String>(this.data); }
}
	
