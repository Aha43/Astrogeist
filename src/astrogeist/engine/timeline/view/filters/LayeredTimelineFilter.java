package astrogeist.engine.timeline.view.filters;

import java.time.Instant;
import java.util.ArrayList;

import astrogeist.engine.abstraction.TimelineFilter;
import astrogeist.engine.abstraction.TimelineView;

public final class LayeredTimelineFilter extends AbstractTimelineFilter {
	private ArrayList<OrCompositeTimelineFilter> layers = new ArrayList<>();
	
	public final void add(String name, TimelineFilter filter) {
		var layer = this.getLayer(name);
		if (layer == null) {
			layer = new OrCompositeTimelineFilter();
			this.layers.add(layer);
		}
		layer.add(filter);
	}
	
	private final OrCompositeTimelineFilter getLayer(String name) {
		var r = this.layers.stream().filter(e -> e.description().equals(name)).findFirst();
		return r.isEmpty() ? null : r.get();
	}
	
	@Override public final boolean accept(Instant time, TimelineView view) {
		var n = this.layers.size();
		for (var i = 0; i < n; i++) if (!this.layers.get(i).accept(time, view)) return false;
		return true;
	}
}
