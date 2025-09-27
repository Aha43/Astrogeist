package astrogeist.engine;

import astrogeist.engine.abstraction.ServiceProvider;
import astrogeist.engine.abstraction.TypeResolver;
import astrogeist.engine.abstraction.timeline.Timeline;
import astrogeist.engine.abstraction.timeline.TimelineNames;
import astrogeist.engine.abstraction.timeline.TimelineValuePool;
import astrogeist.engine.scanner.DefaultTimelineNames;
import astrogeist.engine.timeline.DefaultTimeline;
import astrogeist.engine.timeline.DefaultTimelineValuePool;
import astrogeist.engine.typesystem.DefaultTypeResolver;
import astrogeist.engine.userdata.UserDataIo;

/**
 * <p>
 *   Simple IoC.
 * </p>
 */
public final class DefaultServiceProvider implements ServiceProvider {
	@Override public final <T> T get(Class<? extends T> clazz) {
		if (clazz == Timeline.class) return clazz.cast(this.timeline);
		if (clazz == UserDataIo.class) return clazz.cast(this.userDataIo);
		if (clazz == TimelineValuePool.class) return clazz.cast(this.timelineValuePool);
		if (clazz == TimelineNames.class) return clazz.cast(this.timelineNames);
		throw new IllegalArgumentException("Unsupported service type: " + clazz.getName());
	}
	
	private final TypeResolver typeResolver = new DefaultTypeResolver(); 
	private final TimelineValuePool timelineValuePool = new DefaultTimelineValuePool(this.typeResolver); 
	private final UserDataIo userDataIo = new UserDataIo(this.timelineValuePool);
	private final TimelineNames timelineNames = new DefaultTimelineNames();
	private final Timeline timeline = new DefaultTimeline(this.timelineValuePool, this.timelineNames);
}
