package astrogeist.engine.appdata.settings;

import static aha.common.util.Cast.asOrThrow;

import java.io.OutputStream;

import astrogeist.engine.appdata.AbstractNameValueXmlAppDataWriter;

public final class SettingsAppDataWriter
	extends AbstractNameValueXmlAppDataWriter  {

	public SettingsAppDataWriter() { super(Settings.class); }

	@Override public final void write(OutputStream out, Object data)
		throws Exception {
		
		var map = asOrThrow(Settings.class, data).asMap();
		super.write(out, map);
	}

}
