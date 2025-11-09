package astrogeist.engine.appdata.settings;

import java.io.InputStream;

import astrogeist.engine.appdata.AbstractNameValueXmlAppDataReader;

public final class SettingsAppDataReader
	extends AbstractNameValueXmlAppDataReader  {

	public SettingsAppDataReader() { super(Settings.class); }

	@Override public final Object read(InputStream in) throws Exception {
		var data = super.load(in);
		var retVal = new Settings(data);
		return retVal;
	}

	@Override public final Object createDefault() { return new Settings(); }

}
