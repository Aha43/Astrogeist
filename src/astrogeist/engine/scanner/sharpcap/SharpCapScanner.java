package astrogeist.engine.scanner.sharpcap;

import astrogeist.engine.scanner.CapDataScanner;

/**
 * <p>
 *   Scan for files produced by SharpCap.
 * </p>
 */
public class SharpCapScanner extends CapDataScanner {
	public SharpCapScanner(String location) { 
		super(location, new SharpCapFileParser()); 
	}
}
