package astrogeist.engine.scanner;

import java.util.Scanner;

import astrogeist.engine.abstraction.Timeline;

/**
 * <p>
 *   Context
 *   {@link Scanner}s work in.
 * </p>
 */
public final class ScannerContext {
	private final Timeline timeline;
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param timeline the {@link Timeline} 
	 *                 {@link Scanner} add timesnap data to.
	 */
	public ScannerContext(Timeline timeline) { this.timeline = timeline; }
	
	/**
	 * <p>
	 *   Gets the {@link Timeline} 
	 *        {@link Scanner} add timesnap data to.
	 * </p>
	 * @return {@link Timeline}.
	 */
	public Timeline timeline() { return this.timeline; }

}
