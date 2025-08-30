package astrogeist.engine.timeline.view.filters;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import astrogeist.engine.abstraction.TimelineFilter;
import astrogeist.engine.abstraction.TimelineView;

public final class LayeredTimelineFilter extends AbstractTimelineFilter {
    private final Map<String, OrCompositeTimelineFilter> layers = new LinkedHashMap<>();

    public final void add(String name, TimelineFilter filter) {
        var layer = this.layers.computeIfAbsent(name, n -> new OrCompositeTimelineFilter(n));
        layer.add(filter);
    }

    public final Optional<OrCompositeTimelineFilter> getLayer(String name) {
        return Optional.ofNullable(this.layers.get(name)); }

    @Override public final boolean accept(Instant time, TimelineView view) {
        return this.layers.values().stream().allMatch(l -> l.accept(time, view)); }
}
