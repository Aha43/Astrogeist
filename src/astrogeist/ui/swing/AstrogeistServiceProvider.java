package astrogeist.ui.swing;

import static aha.common.guard.CollectionGuards.requireKeyNotExists;
import static aha.common.util.Cast.as;
import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;

import aha.common.abstraction.io.appdata.AppDataManager;
import aha.common.io.appdata.DefaultAppDataManager;
import astrogeist.engine.abstraction.ServiceProvider;
import astrogeist.engine.abstraction.TypeResolver;
import astrogeist.engine.abstraction.selection.SnapshotSelectionService;
import astrogeist.engine.abstraction.timeline.Timeline;
import astrogeist.engine.abstraction.timeline.TimelineNames;
import astrogeist.engine.abstraction.timeline.TimelineValuePool;
import astrogeist.engine.appdata.AstrogeistDiskAppDataAccessor;
import astrogeist.engine.appdata.runconfig.RunConfigurationsAppDataReader;
import astrogeist.engine.appdata.settings.SettingsAppDataReader;
import astrogeist.engine.appdata.settings.SettingsAppDataWriter;
import astrogeist.engine.appdata.userdatadefinitions.UserDataDefinitionsAppDataReader;
import astrogeist.engine.appdats.scannerconfig.ScannerConfigAppDataReader;
import astrogeist.engine.observatory.AhaObservatory;
import astrogeist.engine.observatory.Observatory;
import astrogeist.engine.scanner.DefaultTimelineNames;
import astrogeist.engine.timeline.DefaultTimeline;
import astrogeist.engine.timeline.DefaultTimelineValuePool;
import astrogeist.engine.typesystem.DefaultTypeResolver;
import astrogeist.engine.userdata.UserDataIo;
import astrogeist.ui.selection.DefaultSnapshotSelectionService;

/**
 * <p>
 *   Simple IoC.
 * </p>
 */
public final class AstrogeistServiceProvider implements ServiceProvider {
	private final Map<Class<?>, Object> some = new HashMap<>();
	
	@Override public final <T> T get(Class<? extends T> clazz) {
		T retVal = null;
		
		if (clazz == Observatory.class)
			retVal = as(clazz, this.observatory);
		if (clazz == Timeline.class) 
			retVal = as(clazz, this.timeline);
		if (clazz == UserDataIo.class) 
			retVal = as(clazz, this.userDataIo);
		if (clazz == TimelineValuePool.class)
			retVal = as(clazz, this.timelineValuePool);
		if (clazz == TimelineNames.class)
			retVal = as(clazz, this.timelineNames);
		if (clazz == SnapshotSelectionService.class)
			retVal = as(clazz, this.snapshotSelectionService);
		if (clazz == AppDataManager.class)
			retVal = as(clazz, this.astrogeistStorageManager);
		
		retVal = retVal == null ? as(clazz, this.some.get(clazz)) : retVal;
		
		if (retVal == null)
			throw new IllegalArgumentException("Unsupported service type: " + 
					clazz.getName());
		
		return retVal;
	}
	
	public void singleton(Object service, Class<?>...classes) {
		var key = requireKeyNotExists(requireNonNull(service).getClass(),
			this.some);
		this.some.put(key, service);
		if (classes != null && classes.length > 0) {
			for (var c : classes) {
				var o = as(c, service);
				if (o == null)
					throw new IllegalArgumentException(
						"service not a " + c.getName());
				this.some.put(c, service);
			}
		}
	}
	
	private final Observatory observatory = new AhaObservatory();
	private final TypeResolver typeResolver = new DefaultTypeResolver(); 
	private final TimelineValuePool timelineValuePool =
		new DefaultTimelineValuePool(this.typeResolver); 
	private final UserDataIo userDataIo =
		new UserDataIo(this.timelineValuePool);
    private final SnapshotSelectionService snapshotSelectionService =
    	new DefaultSnapshotSelectionService();
    private final AppDataManager astrogeistStorageManager =
    	new DefaultAppDataManager(
    		new AstrogeistDiskAppDataAccessor(), 
    		new ScannerConfigAppDataReader(),
    		new UserDataDefinitionsAppDataReader(),
    		new SettingsAppDataReader(),
    		new SettingsAppDataWriter(),
    		new RunConfigurationsAppDataReader());
    private final TimelineNames timelineNames =
    	new DefaultTimelineNames(this.astrogeistStorageManager);
    private final Timeline timeline =
    	new DefaultTimeline(this.timelineValuePool, this.timelineNames);
}
