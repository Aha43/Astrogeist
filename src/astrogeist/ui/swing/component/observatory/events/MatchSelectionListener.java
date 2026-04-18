package astrogeist.ui.swing.component.observatory.events;

import astrogeist.engine.Observatory.Match;

public interface MatchSelectionListener {
	void selectionChanged(Match selectedMatch);
}
