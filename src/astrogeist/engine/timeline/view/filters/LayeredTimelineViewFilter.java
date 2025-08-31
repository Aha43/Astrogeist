package astrogeist.engine.timeline.view.filters;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import astrogeist.engine.abstraction.TimelineViewFilter;
import astrogeist.engine.abstraction.TimelineView;

public final class LayeredTimelineViewFilter extends AbstractTimelineViewFilter {
    private final Map<String, OrCompositeTimelineViewFilter> layers = new LinkedHashMap<>();

    public final void add(String name, TimelineViewFilter filter) {
        var layer = this.layers.computeIfAbsent(name, n -> new OrCompositeTimelineViewFilter(n));
        layer.add(filter);
    }

    public final Optional<OrCompositeTimelineViewFilter> getLayer(String name) {
        return Optional.ofNullable(this.layers.get(name)); }

    @Override public final boolean accept(Instant time, TimelineView view) {
        return this.layers.values().stream().allMatch(l -> l.accept(time, view)); }
}
