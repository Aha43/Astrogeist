package astrogeist.engine.scanner.userdata;

import java.util.logging.Level;

import astrogeist.common.async.CancellationToken;
import astrogeist.engine.abstraction.jobs.JobProgressListener;
import astrogeist.engine.abstraction.timeline.Timeline;
import astrogeist.engine.abstraction.timeline.TimelineValuePool;
import astrogeist.engine.resources.Resources;
import astrogeist.engine.scanner.AbstractScanner;
import astrogeist.engine.userdata.UserDataIo;
import astrogeist.engine.util.FilesUtil;
import astrogeist.engine.util.Instants;

public final class UserDataScanner extends AbstractScanner {
	private final UserDataIo userDataIo;
	
	public UserDataScanner(TimelineValuePool tvp) {
		super(Resources.getUserDataDir());
		this.userDataIo = new UserDataIo(tvp); 
	}
	
	@Override public final String name() { return "User data scanner"; }
	
	@Override public final String description() { 
		return "Scanning : '" + Resources.getUserDataDir() + "'"; }

	@Override public final void run(
		Timeline input,
		JobProgressListener listener,
		CancellationToken token) {
		
		var timeline = input;
		
		var root = Resources.getUserDataDir();
		var files = root.listFiles();
		
		listener.onStart(files.length);
			
		for (var file : files) {
			if (token.isCancelled()) break;
			
			try {
				this.logger.info("analyze path: " + file.toString());
				
				var name = FilesUtil.getBaseName(file);
				var t = Instants.fromFileSafeString(name);
				var userData = this.userDataIo.load(t);
				timeline.putTimelineValues(t, userData);
				
				listener.onSuccess(file, null);
			
			} catch (Exception x) {
				listener.onFailure(file, x);
				this.logger.log(Level.WARNING, "Failed analyzing", x);
			}
		}
	}

}
