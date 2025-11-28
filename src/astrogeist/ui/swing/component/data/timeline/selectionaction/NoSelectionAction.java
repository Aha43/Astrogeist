package astrogeist.ui.swing.component.data.timeline.selectionaction;

import astrogeist.engine.timeline.Snapshot;

/**
 * <p>
 *   Singleton 'do nothing' 
 *   {@link AbstractSelectionAction} used in selection of action for end user to
 *   select 'do nothing' on snapshot selection.
 * </p>
 */
public final class NoSelectionAction extends AbstractSelectionAction {
    
	/**
	 * <p>
	 *   The singleton.
	 * </p>
	 */
	public static final NoSelectionAction INSTANCE = new NoSelectionAction();
    
    private NoSelectionAction() { super("— No action —"); }
	@Override public void Perform(Snapshot snapshot) { }
}
