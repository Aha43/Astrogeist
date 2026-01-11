package astrogeist.ui.swing;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.LinkedHashMap;

import astrogeist.engine.abstraction.TimelineManager;
import astrogeist.engine.abstraction.timeline.Timeline;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.ui.swing.component.data.timeline.TimelineTablePanel;
import astrogeist.ui.swing.component.data.timeline.filtering.FilteredTimelineViewTablePanel;

final class DefaultTimelineManager implements TimelineManager {

	private final FilteredTimelineViewTablePanel filteredTimelineViewTablePanel;
	private final TimelineTablePanel timelineTablePanel;
	
	DefaultTimelineManager(
		FilteredTimelineViewTablePanel filteredTimelineViewTablePanel,
		TimelineTablePanel timelineTablePanel) {
		
		this.filteredTimelineViewTablePanel =
			requireNonNull(filteredTimelineViewTablePanel);
		this.timelineTablePanel = timelineTablePanel = 
			requireNonNull(timelineTablePanel) ;
	}
	
	
	@Override public final void timeline(Timeline timeline) {
		filteredTimelineViewTablePanel.timeline(timeline);
		timelineTablePanel.timeline(timeline);
	}

	@Override public final void update(Instant t,
		LinkedHashMap<String, TimelineValue> values) {
		
		timelineTablePanel.update(t, values);
	}

	@Override public final void update(Instant t, String name,
		TimelineValue value) {
		
		timelineTablePanel.update(t, name, value);
	}

}
