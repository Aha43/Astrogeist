package astrogeist.engine.persitence.scannerconfig;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import aha.common.Empty;
import astrogeist.engine.abstraction.Scanner;
import astrogeist.engine.logging.Log;

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
	private final Logger logger = Log.get(this);
	
	private final Map<String, List<String>> configuration;
	
	public ScanningConfiguration() { this.configuration = new HashMap<>(); }
	
	public ScanningConfiguration(Map<String, List<String>> configuration) {
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
	
	/**
	 * <p>
	 *   Creates scanners from configuration.
	 * </p>
	 * @return the created
	 *         {@link Scanner}s.
	 * @throws Exception If fails.
	 */
	public final List<Scanner> createScanners() throws Exception {
        List<Scanner> scanners = new ArrayList<>();
        for (var entry : this.configuration.entrySet()) {
            var type = entry.getKey();
            var locations = entry.getValue();
            var scannersForType = this.resolveFactory(type, locations);
            scanners.addAll(scannersForType);
        }
        return scanners;
    }
	
	/**
     * <p>
     *   Resolves a 
     *   {@link PluginScanner} by its qualified class name.
     * </p>
     * @param qcn the Qualified Class Name of 
     *            {@link PluginScanner} component to resolve.
     * @param locations the "locations" arguments to scanner's constructor.  
     */
    private List<Scanner> resolveFactory(String qcn, List<String> locations) throws Exception {
		this.logger.info("Load scanners type : '" + qcn + "'");
		
		Class<?> raw = Class.forName(qcn);
		Class<? extends Scanner> cls = raw.asSubclass(Scanner.class);
		Constructor<? extends Scanner> ctor = cls.getDeclaredConstructor(String.class);
		ctor.setAccessible(true);
		var retVal = new ArrayList<Scanner>();
		for (var l : locations) {
			this.logger.info("Create scanner for location: '" + l + "'");
			var scanner = Optional.of(ctor.newInstance(l));
			if (scanner.isPresent()) retVal.add(scanner.get());
		}
		return retVal;
    }

}
