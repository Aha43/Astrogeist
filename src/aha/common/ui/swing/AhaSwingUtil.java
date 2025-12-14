package aha.common.ui.swing;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;
import static javax.swing.SwingUtilities.isEventDispatchThread;

/**
 * <p>
 *   Utilities methods of use when working with Swing. 
 * </p>
 */
public final class AhaSwingUtil {
	private AhaSwingUtil() { throwStaticClassInstantiateError(); }
	
	/**
	 * <p>
	 *   Invoke when must be on the Event Dispatching Thread (EDT).
	 * </p>
	 * @throws IllegalStateException If is not on the EDT.
	 */
	public static final void ensureEdt() {
        if (!isEventDispatchThread()) {
            throw new IllegalStateException("Must be called on EDT");
        }
    }

}
