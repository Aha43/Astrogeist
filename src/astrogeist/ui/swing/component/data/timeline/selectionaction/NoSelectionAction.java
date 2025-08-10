package astrogeist.ui.swing.component.data.timeline.selectionaction;

import java.util.Map;

import astrogeist.engine.timeline.TimelineValue;

public final class NoSelectionAction extends AbstractSelectionAction {
    public static final NoSelectionAction INSTANCE = new NoSelectionAction();
    
    private NoSelectionAction() { super("— No action —"); }
    
	@Override public void Perform(Map<String, TimelineValue> snapshot) { }
}
