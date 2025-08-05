package astrogeist.engine.scanner.userdata;

import astrogeist.engine.resources.Resources;
import astrogeist.engine.scanner.AbstractScanner;
import astrogeist.engine.timeline.Timeline;
import astrogeist.engine.userdata.UserDataIo;
import astrogeist.engine.util.FilesUtil;
import astrogeist.engine.util.Instants;

public final class UserDataScanner extends AbstractScanner {
	public UserDataScanner() { super(Resources.getUserDataDir()); }

	@Override
	public void scan(Timeline rename) {
		try {
			var files = super.getRootDir().listFiles();
			for (var file : files) {
				var name = FilesUtil.getBaseName(file);
				var t = Instants.fromFileSafeString(name);
				var userData = UserDataIo.load(t);
				rename.put(t, userData);
			}
		} catch (Exception x) {
			System.err.println(x.getLocalizedMessage());
		}
	}

}
