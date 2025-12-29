package astrogeist.ui.swing;

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
	@Override public final <T> T get(Class<? extends T> clazz) {
		if (clazz == Observatory.class)
			return clazz.cast(this.observatory);
		if (clazz == Timeline.class) 
			return clazz.cast(this.timeline);
		if (clazz == UserDataIo.class) 
			return clazz.cast(this.userDataIo);
		if (clazz == TimelineValuePool.class)
			return clazz.cast(this.timelineValuePool);
		if (clazz == TimelineNames.class)
			return clazz.cast(this.timelineNames);
		if (clazz == SnapshotSelectionService.class)
			return clazz.cast(this.snapshotSelectionService);
		if (clazz == AppDataManager.class)
			return clazz.cast(this.astrogeistStorageManager);
		throw new IllegalArgumentException("Unsupported service type: " + 
			clazz.getName());
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
