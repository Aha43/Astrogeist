package astrogeist.engine.persitence.settings;

import java.io.InputStream;

import astrogeist.engine.persitence.AbstractNameValueXmlAstrogeistDataReader;

public final class SettingsAstrogeistDataReader extends AbstractNameValueXmlAstrogeistDataReader  {

	public SettingsAstrogeistDataReader() { super(Settings.class); }

	@Override public final Object read(InputStream in) throws Exception {
		var data = super.load(in);
		var retVal = new Settings(data);
		return retVal;
	}

	@Override public final Object createDefault() { return new Settings(); }

}
