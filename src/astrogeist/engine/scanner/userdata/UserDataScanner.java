package astrogeist.engine.scanner.userdata;

import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.resources.Resources;
import astrogeist.engine.scanner.AbstractScanner;
import astrogeist.engine.util.FilesUtil;
import astrogeist.engine.util.Instants;
import astrogeist.engine.abstraction.UserDataIo;

public final class UserDataScanner extends AbstractScanner {
	private final UserDataIo userDataIo;
	
	public UserDataScanner(UserDataIo userDataIo) { 
		super(Resources.getUserDataDir()); 
		this.userDataIo = userDataIo;
	}

	@Override public void scan(Timeline timeline) {
		try {
			var files = super.rootDir.listFiles();
			for (var file : files) {
				var name = FilesUtil.getBaseName(file);
				var t = Instants.fromFileSafeString(name);
				var userData = this.userDataIo.load(t);
				timeline.putTimelineValues(t, userData);
			}
		} catch (Exception x) {
			System.err.println(x.getLocalizedMessage());
		}
	}

}
