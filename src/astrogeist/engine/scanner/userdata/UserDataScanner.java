package astrogeist.engine.scanner.userdata;

import astrogeist.engine.abstraction.Scanner;
import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.abstraction.TimelineValuePool;
import astrogeist.engine.resources.Resources;
import astrogeist.engine.userdata.UserDataIo;
import astrogeist.engine.util.FilesUtil;
import astrogeist.engine.util.Instants;

public final class UserDataScanner implements Scanner {
	private final UserDataIo userDataIo;
	
	public UserDataScanner(TimelineValuePool tvp) {
		this.userDataIo = new UserDataIo(tvp); }

	@Override public void scan(Timeline timeline) {
		try {
			var root = Resources.getUserDataDir();
			var files = root.listFiles();
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
