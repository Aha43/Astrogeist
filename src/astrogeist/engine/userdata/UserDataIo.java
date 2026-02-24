package astrogeist.engine.userdata;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import astrogeist.engine.abstraction.timeline.TimelineValuePool;
import astrogeist.engine.resources.Resources;
import astrogeist.engine.timeline.TimelineValue;

public final class UserDataIo {
	private final TimelineValuePool timelineValuePool;
	
	public UserDataIo(TimelineValuePool timelineValuePool) {
		this.timelineValuePool = timelineValuePool; }
	
	public final Map<String, TimelineValue> load(Instant t) 
		throws Exception {
		
		var file = Resources.getUserDataFile(requireNonNull(t, "t"));
		if (!file.exists()) file.createNewFile();
		var retVal = UserDataXml.parse(this.timelineValuePool, file);
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
	
	public final void save(Instant t, Map<String, TimelineValue> userData)
		throws Exception {
		
		requireNonNull(t, "t");
		requireNonNull(userData, "userData");
		
		var file = Resources.getUserDataFile(t);
		var valuesToSave = new LinkedHashMap<>(userData);
		removeDeleted(valuesToSave);
		UserDataXml.serialize(valuesToSave, file);
	}
	
	private static void removeDeleted(LinkedHashMap<String, 
		TimelineValue> userData) {
		
		var keys = new ArrayList<String>();
		for (var e : userData.entrySet())
			if (e.getValue().noData()) keys.add(e.getKey());
		for (var k : keys) userData.remove(k);
	}
	
}
