package astrogeist.engine.scanner;

import java.util.List;
import java.util.Map;

import astrogeist.common.Empty;

/**
 * <p>
 *   Configuration of scanners:
 *   <ul>
 *   	<li>
 *   		{@link #scanners()} returns a list of names of scanners that can be used.
 *   	</li>
 *   	<li>
 *   		For a given scanner named by
 *   		{@link #scanners()} 
 *   		{@link #locations(String)} gets a list of locations to create scanners of 
 *   		the named scanner's type.
 *   	</li>
 *   </ul>
 * </p>
 */
public final class ScanningConfiguration {
	private final Map<String, List<String>> configuration;
	
	ScanningConfiguration(Map<String, List<String>> configuration) {
		this.configuration = configuration; }
	
	/**
	 * <p>
	 *   Gets the name of the scanners of the configuration.
	 * </p>
	 * @return Names.
	 */
	public final String[] scanners() { 
		return this.configuration.keySet().toArray(Empty.StringArray); }
	
	/**
	 * <p>
	 *   Gets the locations for a named scanner to scan.
	 * </p>
	 * @param scanner the name of the scanner.
	 * @return Locations.
	 */
	public final String[] locations(String scanner) {
		var locs = this.configuration.get(scanner);
		if (locs != null) locs.toArray(Empty.StringArray);
		throw new IllegalArgumentException("Scanner : '" + scanner + "' not found");
	}

}
