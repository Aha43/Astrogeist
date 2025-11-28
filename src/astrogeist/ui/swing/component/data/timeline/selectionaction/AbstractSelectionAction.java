package astrogeist.ui.swing.component.data.timeline.selectionaction;

import astrogeist.engine.timeline.Snapshot;

public abstract class AbstractSelectionAction {
	private final String name;
	
	public AbstractSelectionAction(String name) { this.name = name; }
	
	public final String getName() { return this.name; }
	
	public abstract void Perform(Snapshot snapshot);
	
	public static AbstractSelectionAction[] EMPTY_ARRAY = 
		new AbstractSelectionAction[0]; 
}
