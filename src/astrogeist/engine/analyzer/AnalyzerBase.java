package astrogeist.engine.analyzer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import astrogeist.engine.abstraction.Analyzer;
import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.timeline.TimelineValue;

public abstract class AnalyzerBase implements Analyzer {
	private final List<Instant> instants = new ArrayList<>();
	
	@Override public final int size() { 
		var n = this.instants.size();
		return n == 0 ? -1 : n;
	}
	
	@Override public final void analyze(Timeline timeline) {
		var instants = this.instants.size() == 0 ? timeline.timestamps() : this.instants;
		for (var t : instants) {
			var snapshot = timeline.snapshot(t);
			this.doAnalyze(timeline, t, snapshot);
		}
	}
	
	protected abstract void doAnalyze(Timeline tiimeline, Instant t, Map<String, TimelineValue> snapshot);

}
