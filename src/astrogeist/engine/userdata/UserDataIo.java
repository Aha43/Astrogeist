package astrogeist.engine.userdata;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import astrogeist.engine.abstraction.timeline.TimelineValuePool;
import astrogeist.engine.resources.Resources;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.util.io.NameValueMapXml;

public final class UserDataIo {
	private final TimelineValuePool timelineValuePool;
	
	public UserDataIo(TimelineValuePool timelineValuePool) {
		this.timelineValuePool = timelineValuePool; }
	
	public final LinkedHashMap<String, TimelineValue> load(Instant t) 
		throws Exception {
		
		var file = Resources.getUserDataFile(requireNonNull(t, "t"));
		if (!file.exists()) file.createNewFile();
		var retVal = NameValueMapXml.loadTimeLineValues(this.timelineValuePool,
			file);
		return retVal;
	}
	
	public final void save(Instant t, String name, TimelineValue tv)
		throws Exception {
		
		requireNonNull(t, "t");
		requireNonEmpty(name, "name");
		requireNonNull(tv, "tv");
		
		var data = this.load(t);
		data.put( name, tv);
		this.save(t,data);
	}
	
	public final void save(Instant t, 
		LinkedHashMap<String, TimelineValue> userData) throws Exception {
		
		var file = Resources.getUserDataFile(requireNonNull(t, "t"));
		var valuesToSave =
			new LinkedHashMap<>(requireNonNull(userData, "userData"));
		removeDeleted(valuesToSave);
		NameValueMapXml.saveTimelineValues(valuesToSave, file);
	}
	
	private static void removeDeleted(LinkedHashMap<String, 
		TimelineValue> userData) {
		
		var keys = new ArrayList<String>();
		for (var e : userData.entrySet())
			if (e.getValue().noData()) keys.add(e.getKey());
		for (var k : keys) userData.remove(k);
	}
}
