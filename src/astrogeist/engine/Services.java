package astrogeist.engine;

import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.abstraction.TimelineValuePool;
import astrogeist.engine.abstraction.TypeResolver;
import astrogeist.engine.timeline.DefaultTimeline;
import astrogeist.engine.timeline.DefaultTimelineValuePool;
import astrogeist.engine.typesystem.DefaultTypeResolver;
import astrogeist.engine.userdata.UserDataIo;

/**
 * <p>
 *   Simple IoC.
 * </p>
 */
public final class Services {
	private final TypeResolver typeResolver = new DefaultTypeResolver(); 
	
	private final TimelineValuePool timelineValuePool = new DefaultTimelineValuePool(this.typeResolver); 
	
	private final Timeline timeline = new DefaultTimeline(this.timelineValuePool);
	
	private final UserDataIo userDataIo = new UserDataIo(this.timelineValuePool);
	
	public Timeline getTimeline() { return this.timeline; }
	
	public UserDataIo getUserDataIo() { return this.userDataIo; }
	
	public TimelineValuePool getTimelineValuePool() { return this.timelineValuePool; }
}
