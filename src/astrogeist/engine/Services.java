package astrogeist.engine;

import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.abstraction.TimelineValuePool;
import astrogeist.engine.abstraction.TimelineView;
import astrogeist.engine.abstraction.TypeResolver;
import astrogeist.engine.abstraction.UserDataIo;
import astrogeist.engine.timeline.DefaultTimeline;
import astrogeist.engine.timeline.DefaultTimelineValuePool;
import astrogeist.engine.timeline.view.CompositeFilteredTimelineView;
import astrogeist.engine.typesystem.DefaultTypeResolver;
import astrogeist.engine.userdata.DefaultUserDataIo;

public final class Services {
	private final TypeResolver typeResolver = new DefaultTypeResolver(); 
	private final TimelineValuePool timelineValuePool = new DefaultTimelineValuePool(this.typeResolver); 
	
	private final Timeline timeline = new DefaultTimeline(this.timelineValuePool);
	private final TimelineView timelineView = new CompositeFilteredTimelineView(timeline);
	
	private final UserDataIo userDataIo = new DefaultUserDataIo(this.timelineValuePool);
	
	public Timeline getTimeline() { return this.timeline; }
	public TimelineView getTimelineView() { return this.timelineView; }
	
	public UserDataIo getUserDataIo() { return this.userDataIo; }
}
