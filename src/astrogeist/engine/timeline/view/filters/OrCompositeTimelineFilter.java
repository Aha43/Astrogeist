package astrogeist.engine.timeline.view.filters;

import java.time.Instant;

import astrogeist.engine.abstraction.TimelineView;

public final class OrCompositeTimelineFilter extends AbstractCompositeTimelineFilter {
	@Override public final boolean accept(Instant time, TimelineView view) {
		var n = super.size();
		for (var i = 0; i < n; i++) if (super.get(i).accept(time, view)) return true;
		return false;
	}	
}
