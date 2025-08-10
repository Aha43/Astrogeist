package astrogeist.ui.swing.component.data.timeline.selectionaction;

import java.util.Map;

import astrogeist.engine.timeline.TimelineValue;

public abstract class AbstractSelectionAction {
	private final String name;
	
	public AbstractSelectionAction(String name) { this.name = name; }
	
	public final String getName() { return this.name; }
	
	public abstract void Perform(Map<String, TimelineValue> snapshot);
	
	public static AbstractSelectionAction[] EMPTY_ARRAY = new AbstractSelectionAction[0]; 
}
