package astrogeist.scanner.userdata;

import astrogeist.resources.Resources;
import astrogeist.scanner.AbstractScanner;
import astrogeist.timeline.Timeline;
import astrogeist.userdata.UserDataIo;
import astrogeist.util.FilesUtil;
import astrogeist.util.Instants;

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
